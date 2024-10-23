/**
 *  Copyright (c) 2013-2016 Angelo ZERR and Genuitec LLC.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Piotr Tomiak <piotr@genuitec.com> - initial API and implementation
 */
package tern.eclipse.ide.internal.core.resources;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import tern.TernException;
import tern.eclipse.ide.core.TernCorePlugin;
import tern.server.ITernServer;
import tern.server.ITernServerRequestProcessor;
import tern.server.TernNoTypeFoundAtPositionException;
import tern.server.protocol.ITernResultsAsyncCollector;
import tern.server.protocol.ITernResultsAsyncCollector.TimeoutReason;
import tern.server.protocol.ITernResultsCollector;
import tern.server.protocol.TernDoc;
import tern.server.protocol.TernResultsProcessorsFactory;

public class IDETernServerAsyncReqProcessor extends Job implements
		ITernServerRequestProcessor {

	private static final long TIMEOUT = 750;

	private ITernResultsAsyncCollector collector;
	private TernDoc doc;
	private final ITernServer server;
	private boolean timedOut;

	public IDETernServerAsyncReqProcessor(ITernServer server) {
		super("Asynchronous server request job"); //$NON-NLS-1$
		this.server = server;
	}

	@Override
	public void processRequest(TernDoc doc, ITernResultsCollector c)
			throws TernException {
		if (!(c instanceof ITernResultsAsyncCollector)) {
			// need to process request synchronously
			try {
				TernResultsProcessorsFactory.makeRequestAndProcess(doc, server,
						c);
				return;
			} catch (TernException ex) {
				throw ex;
			} catch (Throwable t) {
				throw new TernException(t);
			}
		}
		ITernResultsAsyncCollector collector = (ITernResultsAsyncCollector) c;
		long start = System.currentTimeMillis();
		while (this.collector != null
				&& (System.currentTimeMillis() - start) < TIMEOUT / 2) {
			// completion calculation is still taking place
			// wait a bit for it to finish
			try {
				Thread.sleep(25);
			} catch (InterruptedException e) {
				break;
			}
		}
		synchronized (this) {
			if (this.collector != null) {
				// Some request is still being processed.
				collector.timeout(TimeoutReason.PREV_OPERATION_NOT_FINISHED);
				return;
			}
			// can schedule
			this.collector = collector;
			this.doc = doc;
			setName(this.collector.getRequestDisplayName());
			timedOut = false;
			schedule();
		}
		while (this.collector == collector
				&& (System.currentTimeMillis() - start) < TIMEOUT) {
			try {
				Thread.sleep(25);
			} catch (InterruptedException e) {
				break;
			}
		}
		synchronized (this) {
			if (this.collector == collector) {
				// will info about time outing only if not finished earlier
				collector.timeout(TimeoutReason.TIMED_OUT);
				timedOut = true;
			}
		}
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		try {
			monitor.beginTask("", IProgressMonitor.UNKNOWN); //$NON-NLS-1$
			TernResultsProcessorsFactory.makeRequestAndProcess(doc, server,
					collector);
		} catch(TernNoTypeFoundAtPositionException e) {
			// ignore error
			// case when user open hyperlink on javascript element like 'function'. In this case tern throws the error
			// tern.TernException: TernError: No type found at the given position.
			// This error must be ignored.
			// See https://github.com/angelozerr/tern.java/issues/392
		} catch (Throwable e) {
				TernCorePlugin.getDefault().getLog()
						.log(new Status(IStatus.ERROR, TernCorePlugin.PLUGIN_ID, e.getMessage(), e));			
		}
		// mark collection as done if not timed out earlier
		synchronized (this) {
			if (!timedOut) {
				collector.done();
			}
			collector = null;
		}
		return Status.OK_STATUS;
	}

}

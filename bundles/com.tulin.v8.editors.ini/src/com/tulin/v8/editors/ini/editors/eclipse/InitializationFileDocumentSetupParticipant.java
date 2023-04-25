/*    */ package com.tulin.v8.editors.ini.editors.eclipse;
/*    */ 
/*    */ import org.eclipse.core.filebuffers.IDocumentSetupParticipant;
/*    */ import org.eclipse.jface.text.IDocument;
/*    */ import org.eclipse.jface.text.IDocumentExtension3;
/*    */ import org.eclipse.jface.text.IDocumentPartitioner;
/*    */ import org.eclipse.jface.text.rules.FastPartitioner;
/*    */ 
/*    */ public class InitializationFileDocumentSetupParticipant
/*    */   implements IDocumentSetupParticipant
/*    */ {
/*    */   public void setup(IDocument document)
/*    */   {
/* 31 */     setupDocument(document);
/*    */   }
/*    */ 
/*    */   public static void setupDocument(IDocument document)
/*    */   {
/* 38 */     IDocumentPartitioner partitioner = createDocumentPartitioner();
/* 39 */     if ((document instanceof IDocumentExtension3)) {
/* 40 */       IDocumentExtension3 extension3 = (IDocumentExtension3)document;
/* 41 */       extension3.setDocumentPartitioner("___pf_partitioning", partitioner);
/*    */     } else {
/* 43 */       document.setDocumentPartitioner(partitioner);
/*    */     }
/* 45 */     partitioner.connect(document);
/*    */   }
/*    */ 
/*    */   private static IDocumentPartitioner createDocumentPartitioner()
/*    */   {
/* 55 */     return new FastPartitioner(new InitializationFilePartitionScanner(), IPropertiesFilePartitions.PARTITIONS);
/*    */   }
/*    */ }

/* Location:           D:\TuLinv8_All\TuLinv8_win64\studio\dropins\propeditor\plugins\org.bogus.propeditor_1.0.8.jar
 * Qualified Name:     org.bogus.propeditor.editors.eclipse.PropertiesFileDocumentSetupParticipant
 * JD-Core Version:    0.6.0
 */
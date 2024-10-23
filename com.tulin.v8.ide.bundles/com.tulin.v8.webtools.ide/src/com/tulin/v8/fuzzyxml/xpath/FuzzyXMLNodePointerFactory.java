package com.tulin.v8.fuzzyxml.xpath;

import java.util.Locale;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.NodePointerFactory;

import com.tulin.v8.fuzzyxml.FuzzyXMLDocument;
import com.tulin.v8.fuzzyxml.FuzzyXMLElement;


public class FuzzyXMLNodePointerFactory implements NodePointerFactory {
    
    public int getOrder() {
        return 0;
    }
    
    public NodePointer createNodePointer(QName name, Object object, Locale locale) {
        if (object instanceof FuzzyXMLDocument) {
            return new FuzzyXMLNodePointer(object, locale);
        } else if (object instanceof FuzzyXMLElement) {
            return new FuzzyXMLNodePointer(object, locale);
        }
        return null;
    }

    public NodePointer createNodePointer(NodePointer parent, QName name, Object object) {
        if (object instanceof FuzzyXMLDocument) {
            return new FuzzyXMLNodePointer(parent, object);
        } else if (object instanceof FuzzyXMLElement) {
            return new FuzzyXMLNodePointer(parent, object);
        }
        return null;
    }

}

package edu.rpi.metpetdb.server.search.lucene.filters;

import java.io.IOException;
import java.util.BitSet;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.search.Filter;

public class ElementAmountFilter extends Filter {

	@Override
	public BitSet bits(IndexReader reader) throws IOException {
		BitSet bitSet = new BitSet( reader.maxDoc() );
        TermDocs termDocs = reader.termDocs( new Term("score", "5") );
        while ( termDocs.next() ) {
            bitSet.set( termDocs.doc() );
        }
        return bitSet;
	}

}

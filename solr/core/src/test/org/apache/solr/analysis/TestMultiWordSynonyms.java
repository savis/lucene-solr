/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.solr.analysis;

import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.lucene.analysis.MockTokenizer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.util.ResourceLoader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @since solr 1.4
 */
public class TestMultiWordSynonyms extends BaseTokenStreamTestCase {
  
  public void testMultiWordSynonyms() throws IOException {
    SynonymFilterFactory factory = new SynonymFilterFactory();
    Map<String,String> args = new HashMap<String,String>();
    args.put("synonyms", "synonyms.txt");
    factory.setLuceneMatchVersion(TEST_VERSION_CURRENT);
    factory.init(args);
    factory.inform(new StringMockSolrResourceLoader("a b c,d"));
    TokenStream ts = factory.create(new MockTokenizer(new StringReader("a e"), MockTokenizer.WHITESPACE, false));
    // This fails because ["e","e"] is the value of the token stream
    assertTokenStreamContents(ts, new String[] { "a", "e" });
  }
  
  private class StringMockSolrResourceLoader implements ResourceLoader {
    String text;

    StringMockSolrResourceLoader(String text) {
      this.text = text;
    }

    public List<String> getLines(String resource) throws IOException {
      return null;
    }

    public <T> T newInstance(String cname, Class<T> expectedType, String... subpackages) {
      return null;
    }

    public InputStream openResource(String resource) throws IOException {
      return new ByteArrayInputStream(text.getBytes("UTF-8"));
    }
  }
}

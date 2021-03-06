/*-
 * ========================LICENSE_START=================================
 * Smooks Core
 * %%
 * Copyright (C) 2020 Smooks
 * %%
 * Licensed under the terms of the Apache License Version 2.0, or
 * the GNU Lesser General Public License version 3.0 or later.
 * 
 * SPDX-License-Identifier: Apache-2.0 OR LGPL-3.0-or-later
 * 
 * ======================================================================
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * ======================================================================
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * =========================LICENSE_END==================================
 */
package org.smooks.JIRAs.MILYN_367;

import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Test;
import org.smooks.FilterSettings;
import org.smooks.Smooks;
import org.smooks.SmooksException;
import org.smooks.StreamFilterType;
import org.smooks.container.ExecutionContext;
import org.smooks.delivery.dom.DOMVisitAfter;
import org.smooks.delivery.dom.DOMVisitBefore;
import org.smooks.delivery.dom.serialize.DefaultSerializationUnit;
import org.smooks.delivery.sax.DefaultSAXElementSerializer;
import org.smooks.payload.StringResult;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;


/**
 * @author <a href="mailto:tom.fennelly@jboss.com">tom.fennelly@jboss.com</a>
 */
public class MILYN_367_Test {

	@Test
	public void test_SAX_01() throws SAXException, IOException {
		Smooks smooks = new Smooks();
		StringResult result = new StringResult();
		
		smooks.addVisitor(new DefaultSAXElementSerializer(), "#document");
		smooks.addVisitor(new DefaultSAXElementSerializer(), "#document/**");
		smooks.setFilterSettings(new FilterSettings().setDefaultSerializationOn(false).setFilterType(StreamFilterType.SAX));
		
		smooks.filterSource(new StreamSource(getClass().getResourceAsStream("order.xml")), result);
		
		assertOK("expected_01.xml", result);        
	}

	@Test
	public void test_SAX_02() throws SAXException, IOException {
		Smooks smooks = new Smooks();
		StringResult result = new StringResult();
		
		smooks.addVisitor(new DefaultSAXElementSerializer(), "customer");
		smooks.addVisitor(new DefaultSAXElementSerializer(), "customer/**");
		smooks.setFilterSettings(new FilterSettings().setDefaultSerializationOn(false).setFilterType(StreamFilterType.SAX));
		
		smooks.filterSource(new StreamSource(getClass().getResourceAsStream("order.xml")), result);
		
		assertOK("expected_02.xml", result);        
	}

	@Test
	public void test_SAX_03() throws SAXException, IOException {
		Smooks smooks = new Smooks();
		StringResult result = new StringResult();
		
		smooks.addVisitor(new DefaultSAXElementSerializer(), "items");
		smooks.addVisitor(new DefaultSAXElementSerializer(), "items/**");
		smooks.setFilterSettings(new FilterSettings().setDefaultSerializationOn(false).setFilterType(StreamFilterType.SAX));
		
		smooks.filterSource(new StreamSource(getClass().getResourceAsStream("order.xml")), result);
		
		assertOK("expected_03.xml", result);        
	}

	@Test
	public void test_DOM_01() throws SAXException, IOException {
		Smooks smooks = new Smooks();
		StringResult result = new StringResult();
		
		smooks.addVisitor(new DefaultSerializationUnit(), "#document");
		smooks.addVisitor(new DefaultSerializationUnit(), "#document/**");
		smooks.setFilterSettings(new FilterSettings().setDefaultSerializationOn(false).setFilterType(StreamFilterType.DOM));
		
		smooks.filterSource(new StreamSource(getClass().getResourceAsStream("order.xml")), result);
		
		assertOK("expected_01.xml", result);        
	}

	@Test
	public void test_DOM_02() throws SAXException, IOException {
		Smooks smooks = new Smooks();
		StringResult result = new StringResult();
		
		smooks.addVisitor(new DefaultSerializationUnit(), "customer");
		smooks.addVisitor(new DefaultSerializationUnit(), "customer/**");
		smooks.setFilterSettings(new FilterSettings().setDefaultSerializationOn(false).setFilterType(StreamFilterType.DOM));
		
		smooks.filterSource(new StreamSource(getClass().getResourceAsStream("order.xml")), result);
		
		assertOK("expected_02.xml", result);        
	}

	@Test
	public void test_DOM_03() throws SAXException, IOException {
		Smooks smooks = new Smooks();
		StringResult result = new StringResult();
		
		smooks.addVisitor(new DefaultSerializationUnit(), "items");
		smooks.addVisitor(new DefaultSerializationUnit(), "items/**");
		smooks.setFilterSettings(new FilterSettings().setDefaultSerializationOn(false).setFilterType(StreamFilterType.DOM));
		
		smooks.filterSource(new StreamSource(getClass().getResourceAsStream("order.xml")), result);
		
		assertOK("expected_03.xml", result);        
	}

	@Test
	public void test_DOM_04() throws SAXException, IOException {
		Smooks smooks = new Smooks();		
		DOMVBefore customerVisitor = new DOMVBefore();
		DOMVBefore itemsVisitor = new DOMVBefore();
		
		smooks.addVisitor(customerVisitor, "customer");
		smooks.addVisitor(customerVisitor, "customer/**");
		smooks.addVisitor(itemsVisitor, "items");
		smooks.addVisitor(itemsVisitor, "items/**");
		smooks.filterSource(new StreamSource(getClass().getResourceAsStream("order.xml")));
		
		assertEquals("customer-user-fname-x-lname-", customerVisitor.stringBuilder.toString());
		assertEquals("items-item-units-name-price-item-units-name-price-", itemsVisitor.stringBuilder.toString());
	}

	@Test
	public void test_DOM_05() throws SAXException, IOException {
		Smooks smooks = new Smooks();		
		DOMVAfter customerVisitor = new DOMVAfter();
		DOMVAfter itemsVisitor = new DOMVAfter();
		
		smooks.addVisitor(customerVisitor, "customer");
		smooks.addVisitor(customerVisitor, "customer/**");
		smooks.addVisitor(itemsVisitor, "items");
		smooks.addVisitor(itemsVisitor, "items/**");
		smooks.filterSource(new StreamSource(getClass().getResourceAsStream("order.xml")));
		
		assertEquals("user-x-fname-lname-customer-", customerVisitor.stringBuilder.toString());
		assertEquals("units-name-price-item-units-name-price-item-items-", itemsVisitor.stringBuilder.toString());
	}

	private void assertOK(String resName, StringResult result) throws SAXException, IOException {
		XMLUnit.setIgnoreWhitespace(true);
		XMLAssert.assertXMLEqual(getRes(resName), new StringReader(result.getResult()));
	}	
	
	private Reader getRes(String name) {
		return new InputStreamReader(getClass().getResourceAsStream(name));
	}
	
	private class DOMVBefore implements DOMVisitBefore {
		
		private final StringBuilder stringBuilder = new StringBuilder();
		
		public void visitBefore(Element element, ExecutionContext executionContext) throws SmooksException {
			stringBuilder.append(element.getLocalName()).append("-");		
		}		
	}
	
	private class DOMVAfter implements DOMVisitAfter {
		
		private final StringBuilder stringBuilder = new StringBuilder();
		
		public void visitAfter(Element element, ExecutionContext executionContext) throws SmooksException {
			stringBuilder.append(element.getLocalName()).append("-");		
		}		
	}
}

/*
	Milyn - Copyright (C) 2006 - 2010

	This library is free software; you can redistribute it and/or
	modify it under the terms of the GNU Lesser General Public
	License (version 2.1) as published by the Free Software
	Foundation.

	This library is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

	See the GNU Lesser General Public License for more details:
	http://www.gnu.org/licenses/lgpl.txt
 */

package org.milyn.csv;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.stream.StreamSource;

import org.junit.Test;
import static org.junit.Assert.*;

import org.milyn.FilterSettings;
import org.milyn.Smooks;
import org.milyn.SmooksException;
import org.milyn.SmooksUtil;
import org.milyn.cdr.SmooksConfigurationException;
import org.milyn.container.ExecutionContext;
import org.milyn.flatfile.Binding;
import org.milyn.flatfile.BindingType;
import org.milyn.payload.JavaResult;
import org.milyn.payload.StringResult;
import org.xml.sax.SAXException;

/**
 * @author tfennelly
 */
public class CSVReaderTest {

    @Test
    public void test_02() throws SmooksException, IOException, SAXException {
        Smooks smooks = new Smooks(getClass().getResourceAsStream("smooks-config-01.xml"));

        ExecutionContext context = smooks.createExecutionContext();
        String result = SmooksUtil.filterAndSerialize(context, getClass().getResourceAsStream("input-message-01.csv"),
                smooks);
        assertEquals(
                "<csv-set><csv-record number=\"1\"><firstname>Tom</firstname><lastname>Fennelly</lastname><gender>Male</gender><age>4</age><country>Ireland</country></csv-record><csv-record number=\"2\"><firstname>Mike</firstname><lastname>Fennelly</lastname><gender>Male</gender><age>2</age><country>Ireland</country></csv-record></csv-set>",
                result);
    }

    @Test
    public void test_03() throws SmooksException, IOException, SAXException {
        test_03("smooks-config-02.xml");
        test_03("smooks-config-03.xml");
    }

    public void test_03(String config) throws SmooksException, IOException, SAXException {
        Smooks smooks = new Smooks(getClass().getResourceAsStream(config));

        ExecutionContext context = smooks.createExecutionContext();
        String result = SmooksUtil.filterAndSerialize(context, getClass().getResourceAsStream("input-message-02.csv"),
                smooks);
        assertEquals(
                "<csv-set><csv-record number=\"1\"><firstname>Tom</firstname><lastname>Fennelly</lastname><gender>Male</gender><age>4</age><country>Ireland</country></csv-record><csv-record number=\"2\"><firstname>Mike</firstname><lastname>Fennelly</lastname><gender>Male</gender><age>2</age><country>Ireland</country></csv-record></csv-set>",
                result);
    }

    @Test
    public void test_04() throws SmooksException, IOException, SAXException {
        Smooks smooks = new Smooks(getClass().getResourceAsStream("smooks-extended-config-04.xml"));

        ExecutionContext context = smooks.createExecutionContext();
        String result = SmooksUtil.filterAndSerialize(context, getClass().getResourceAsStream("input-message-03.csv"),
                smooks);
        assertEquals(
                "<csv-set><csv-record number=\"1\"><firstname>Tom</firstname><lastname>Fennelly</lastname><gender>Male</gender><age>4</age><country>Ireland</country></csv-record><csv-record number=\"2\"><firstname>Mike</firstname><lastname>Fennelly</lastname><gender>Male</gender><age>2</age><country>Ireland</country></csv-record></csv-set>",
                result);
    }

    @Test
    public void test_05() throws SmooksException, IOException, SAXException {
        Smooks smooks = new Smooks(getClass().getResourceAsStream("smooks-extended-config-05.xml"));

        ExecutionContext context = smooks.createExecutionContext("A");

        String result = SmooksUtil.filterAndSerialize(context, getClass().getResourceAsStream("input-message-03.csv"),
                smooks);
        assertEquals(
                "<csv-set><csv-record number=\"1\"><firstname>Tom</firstname><lastname>Fennelly</lastname><gender>Male</gender><age>4</age><country>Ireland</country></csv-record><csv-record number=\"2\"><firstname>Mike</firstname><lastname>Fennelly</lastname><gender>Male</gender><age>2</age><country>Ireland</country></csv-record></csv-set>",
                result);

        context = smooks.createExecutionContext("B");

        result = SmooksUtil.filterAndSerialize(context, getClass().getResourceAsStream("input-message-04.csv"), smooks);
        assertEquals(
                "<csv-set><csv-record number=\"1\"><firstname>Tom</firstname><lastname>Fennelly</lastname><gender>Male</gender><age>4</age><country>Ireland</country></csv-record><csv-record number=\"2\"><firstname>Mike</firstname><lastname>Fennelly</lastname><gender>Male</gender><age>2</age><country>Ireland</country></csv-record></csv-set>",
                result);
    }

    @Test
    public void test_06() throws SmooksException, IOException, SAXException {
        Smooks smooks = new Smooks(getClass().getResourceAsStream("smooks-extended-config-06.xml"));

        ExecutionContext context = smooks.createExecutionContext();
        String result = SmooksUtil.filterAndSerialize(context, getClass().getResourceAsStream("input-message-03.csv"),
                smooks);
        assertEquals(
                "<customers><customer number=\"1\"><firstname>Tom</firstname><lastname>Fennelly</lastname><gender>Male</gender><age>4</age><country>Ireland</country></customer><customer number=\"2\"><firstname>Mike</firstname><lastname>Fennelly</lastname><gender>Male</gender><age>2</age><country>Ireland</country></customer></customers>",
                result);
    }

    @Test
    public void test_07() throws SmooksException, IOException, SAXException {
        Smooks smooks = new Smooks();

        smooks.setReaderConfig(new CSVRecordParserConfigurator("firstname,lastname,gender,age,country"));

        StringResult result = new StringResult();
        smooks.filterSource(new StreamSource(getClass().getResourceAsStream("input-message-01.csv")), result);

        assertEquals(
                "<csv-set><csv-record number=\"1\"><firstname>Tom</firstname><lastname>Fennelly</lastname><gender>Male</gender><age>4</age><country>Ireland</country></csv-record><csv-record number=\"2\"><firstname>Mike</firstname><lastname>Fennelly</lastname><gender>Male</gender><age>2</age><country>Ireland</country></csv-record></csv-set>",
                result.getResult());
    }

    @Test
    public void test_08() throws SmooksException, IOException, SAXException {
        Smooks smooks = new Smooks();

        smooks.setReaderConfig(new CSVRecordParserConfigurator("firstname,lastname,gender,age,country")
                .setSeparatorChar('|').setQuoteChar('\'').setSkipLineCount(1).setRootElementName("customers")
                .setRecordElementName("customer"));

        StringResult result = new StringResult();
        smooks.filterSource(new StreamSource(getClass().getResourceAsStream("input-message-03.csv")), result);

        assertEquals(
                "<customers><customer number=\"1\"><firstname>Tom</firstname><lastname>Fennelly</lastname><gender>Male</gender><age>4</age><country>Ireland</country></customer><customer number=\"2\"><firstname>Mike</firstname><lastname>Fennelly</lastname><gender>Male</gender><age>2</age><country>Ireland</country></customer></customers>",
                result.getResult());
    }

    @Test
    public void test_09_1() throws SmooksException, IOException, SAXException {
        Smooks smooks = new Smooks(getClass().getResourceAsStream("smooks-extended-config-07.xml"));

        JavaResult result = new JavaResult();
        smooks.filterSource(new StreamSource(getClass().getResourceAsStream("input-message-05.csv")), result);

        Person person = (Person) result.getBean("person");
        assertEquals("(Linda, Coughlan, Ireland, Female, 22)", person.toString());
    }

    @Test
    public void test_09_2() throws SmooksException, IOException, SAXException {
        Smooks smooks = new Smooks(getClass().getResourceAsStream("smooks-extended-config-08.xml"));

        JavaResult result = new JavaResult();
        smooks.filterSource(new StreamSource(getClass().getResourceAsStream("input-message-05.csv")), result);

        List<Person> people = (List<Person>) result.getBean("people");
        assertEquals(
                "[(Tom, Fennelly, Ireland, Male, 4), (Mike, Fennelly, Ireland, Male, 2), (Linda, Coughlan, Ireland, Female, 22)]",
                people.toString());
    }

    @Test
    public void test_10() throws SmooksException, IOException, SAXException {
        Smooks smooks = new Smooks();

        smooks.setReaderConfig(new CSVRecordParserConfigurator("firstname,lastname,$ignore$,gender,age,country")
                .setBinding(new Binding("people", Person.class, BindingType.LIST)));

        JavaResult result = new JavaResult();
        smooks.filterSource(new StreamSource(getClass().getResourceAsStream("input-message-05.csv")), result);

        List<Person> people = (List<Person>) result.getBean("people");
        assertEquals(
                "[(Tom, Fennelly, Ireland, Male, 4), (Mike, Fennelly, Ireland, Male, 2), (Linda, Coughlan, Ireland, Female, 22)]",
                people.toString());
    }

    @Test
    public void test_11() throws SmooksException, IOException, SAXException {
        Smooks smooks = new Smooks();

        smooks.setReaderConfig(new CSVRecordParserConfigurator("firstname,lastname,$ignore$,gender,age,country")
                .setBinding(new Binding("person", Person.class, BindingType.SINGLE)));

        JavaResult result = new JavaResult();
        smooks.filterSource(new StreamSource(getClass().getResourceAsStream("input-message-05.csv")), result);

        Person person = (Person) result.getBean("person");
        assertEquals("(Linda, Coughlan, Ireland, Female, 22)", person.toString());
    }

    @Test
    public void test_12() throws SmooksException, IOException, SAXException {
        Smooks smooks = new Smooks();

        smooks.setReaderConfig(new CSVRecordParserConfigurator("firstname,lastname,$ignore$,gender,age,country")
                .setBinding(new Binding("people", HashMap.class, BindingType.LIST)));

        JavaResult result = new JavaResult();
        smooks.filterSource(new StreamSource(getClass().getResourceAsStream("input-message-05.csv")), result);

        List<Map> people = (List<Map>) result.getBean("people");
        Map person;

        assertEquals(3, people.size());

        person = people.get(0);
        assertEquals("Tom", person.get("firstname"));
        assertEquals("Fennelly", person.get("lastname"));
        assertEquals("Male", person.get("gender"));
        assertEquals("4", person.get("age"));
        assertEquals("Ireland", person.get("country"));

        person = people.get(1);
        assertEquals("Mike", person.get("firstname"));
        assertEquals("Fennelly", person.get("lastname"));
        assertEquals("Male", person.get("gender"));
        assertEquals("2", person.get("age"));
        assertEquals("Ireland", person.get("country"));

        person = people.get(2);
        assertEquals("Linda", person.get("firstname"));
        assertEquals("Coughlan", person.get("lastname"));
        assertEquals("Female", person.get("gender"));
        assertEquals("22", person.get("age"));
        assertEquals("Ireland", person.get("country"));
    }

    @Test
    public void test_13_xml_dom() throws SmooksException, IOException, SAXException {
        test_13_xml(FilterSettings.DEFAULT_DOM);
    }

    @Test
    public void test_13_xml_sax() throws SmooksException, IOException, SAXException {
        test_13_xml(FilterSettings.DEFAULT_SAX);
    }

    @Test
    public void test_13_programmatic_dom() throws SmooksException, IOException, SAXException {
        test_13_programmatic(FilterSettings.DEFAULT_DOM);
    }

    @Test
    public void test_13_programmatic_sax() throws SmooksException, IOException, SAXException {
        test_13_programmatic(FilterSettings.DEFAULT_SAX);
    }

    public void test_13_xml(FilterSettings filterSettings) throws SmooksException, IOException, SAXException {
        Smooks smooks = new Smooks(getClass().getResourceAsStream("smooks-extended-config-09.xml"));
        smooks.setFilterSettings(filterSettings);
        test_13(smooks);
    }

    public void test_13_programmatic(FilterSettings filterSettings) throws SmooksException, IOException, SAXException {
        Smooks smooks = new Smooks();

        smooks.setReaderConfig(new CSVRecordParserConfigurator("firstname,lastname,$ignore$,gender,age,country")
                .setBinding(new Binding("people", Person.class, BindingType.MAP).setKeyField("age")));

        smooks.setFilterSettings(filterSettings);
        test_13(smooks);
    }

    private void test_13(Smooks smooks) {
        JavaResult result = new JavaResult();
        smooks.filterSource(new StreamSource(getClass().getResourceAsStream("input-message-05.csv")), result);

        Map<Integer, Person> people = (Map<Integer, Person>) result.getBean("people");
        Person person;

        person = people.get(4);
        assertEquals("(Tom, Fennelly, Ireland, Male, 4)", person.toString());
        person = people.get(2);
        assertEquals("(Mike, Fennelly, Ireland, Male, 2)", person.toString());
        person = people.get(22);
        assertEquals("(Linda, Coughlan, Ireland, Female, 22)", person.toString());
    }

    @Test
    public void test_14_xml_dom() throws SmooksException, IOException, SAXException {
        test_14_xml(FilterSettings.DEFAULT_DOM);
    }

    @Test
    public void test_14_xml_sax() throws SmooksException, IOException, SAXException {
        test_14_xml(FilterSettings.DEFAULT_SAX);
    }

    @Test
    public void test_14_programmatic_dom() throws SmooksException, IOException, SAXException {
        test_14_programmatic(FilterSettings.DEFAULT_DOM);
    }

    @Test
    public void test_14_programmatic_sax() throws SmooksException, IOException, SAXException {
        test_14_programmatic(FilterSettings.DEFAULT_SAX);
    }

    public void test_14_xml(FilterSettings filterSettings) throws SmooksException, IOException, SAXException {
        Smooks smooks = new Smooks(getClass().getResourceAsStream("smooks-extended-config-10.xml"));
        smooks.setFilterSettings(filterSettings);
        test_14(smooks);
    }

    public void test_14_programmatic(FilterSettings filterSettings) throws SmooksException, IOException, SAXException {
        Smooks smooks = new Smooks();

        smooks.setReaderConfig(new CSVRecordParserConfigurator("firstname,lastname,$ignore$,gender,age,country")
                .setBinding(new Binding("people", HashMap.class, BindingType.MAP).setKeyField("firstname")));

        smooks.setFilterSettings(filterSettings);
        test_14(smooks);
    }

    private void test_14(Smooks smooks) {
        JavaResult result = new JavaResult();
        smooks.filterSource(new StreamSource(getClass().getResourceAsStream("input-message-05.csv")), result);

        Map<String, Map> people = (Map<String, Map>) result.getBean("people");
        Map person;

        person = people.get("Tom");
        assertEquals("Tom", person.get("firstname"));
        assertEquals("Fennelly", person.get("lastname"));
        assertEquals("Male", person.get("gender"));
        assertEquals("4", person.get("age"));
        assertEquals("Ireland", person.get("country"));

        person = people.get("Mike");
        assertEquals("Mike", person.get("firstname"));
        assertEquals("Fennelly", person.get("lastname"));
        assertEquals("Male", person.get("gender"));
        assertEquals("2", person.get("age"));
        assertEquals("Ireland", person.get("country"));

        person = people.get("Linda");
        assertEquals("Linda", person.get("firstname"));
        assertEquals("Coughlan", person.get("lastname"));
        assertEquals("Female", person.get("gender"));
        assertEquals("22", person.get("age"));
        assertEquals("Ireland", person.get("country"));
    }

    @Test
    public void test_15() throws SmooksException, IOException, SAXException {
        Smooks smooks = new Smooks(getClass().getResourceAsStream("smooks-extended-config-11.xml"));

        JavaResult result = new JavaResult();
        try {
            smooks.filterSource(new StreamSource(getClass().getResourceAsStream("input-message-05.csv")), result);
            fail("Expected SmooksConfigurationException");
        } catch (SmooksConfigurationException e) {
            assertEquals("Invalid field name 'xxxx'.  Valid names: [firstname, lastname, gender, age, country].",
                    e.getMessage());
        }
    }

    @Test
    public void test_16() throws SmooksException, IOException, SAXException {
        Smooks smooks = new Smooks(getClass().getResourceAsStream("smooks-config-12.xml"));

        ExecutionContext context = smooks.createExecutionContext();
        String result = SmooksUtil.filterAndSerialize(context, getClass().getResourceAsStream("input-message-01.csv"),
                smooks);

        assertEquals(
                "<main-set><record number=\"1\"><firstname>Tom</firstname><lastname>Fennelly</lastname><gender>Male</gender><age>4</age><country>Ireland</country></record><record number=\"2\"><firstname>Mike</firstname><lastname>Fennelly</lastname><gender>Male</gender><age>2</age><country>Ireland</country></record></main-set>",
                result);
    }

    @Test
    public void test_17() throws SmooksException, IOException, SAXException {
        Smooks smooks = new Smooks(getClass().getResourceAsStream("smooks-config-13.xml"));

        ExecutionContext context = smooks.createExecutionContext();
        String result = SmooksUtil.filterAndSerialize(context, getClass().getResourceAsStream("input-message-13.csv"),
                smooks);

        assertEquals(
                "<main-set><record number=\"1\"><firstname>Tom</firstname><lastname>Fennelly</lastname><gender>Male</gender><age>4</age><country>Ireland</country></record><record number=\"2\"><firstname>Mike</firstname><lastname>Fennelly</lastname><gender>Male</gender><age>2</age><country>Ireland</country></record></main-set>",
                result);

        smooks = new Smooks(getClass().getResourceAsStream("smooks-config-12.xml"));

        context = smooks.createExecutionContext();
        result = SmooksUtil.filterAndSerialize(context, getClass().getResourceAsStream("input-message-13.csv"), smooks);

        assertNotSame(
                "<main-set><record number=\"1\"><firstname>Tom</firstname><lastname>Fennelly</lastname><gender>Male</gender><age>4</age><country>Ireland</country></record><record number=\"2\"><firstname>Mike</firstname><lastname>Fennelly</lastname><gender>Male</gender><age>2</age><country>Ireland</country></record></main-set>",
                result);

    }

    @Test
    public void test_17_wildcard() throws SmooksException, IOException, SAXException {
        Smooks smooks = new Smooks(getClass().getResourceAsStream("smooks-config-13-wildcard.xml"));
        StringResult result = new StringResult();

        smooks.filterSource(new StreamSource(getClass().getResourceAsStream("input-message-13.csv")), result);

        assertEquals(
                "<main-set><record number=\"1\"><field_0>Tom</field_0><field_1>Fennelly</field_1><field_2>Male</field_2><field_3>A</field_3><field_4>B</field_4><field_5>C</field_5><field_6>4</field_6><field_7>IR</field_7><field_8>Ireland</field_8><field_9>2</field_9><field_10>3</field_10></record><record number=\"2\"><field_0>Mike</field_0><field_1>Fennelly</field_1><field_2>Male</field_2><field_3>D</field_3><field_4>F</field_4><field_5>G</field_5><field_6>2</field_6><field_7>IR</field_7><field_8>Ireland</field_8><field_9>4</field_9></record></main-set>",
                result.toString());
    }

    @Test
    public void test_18() throws SmooksException, IOException, SAXException {
        Smooks smooks = new Smooks();

        smooks.setReaderConfig(new CSVRecordParserConfigurator("firstname?upper_case,lastname?uncap_first,$ignore$5")
                .setBinding(new Binding("people", HashMap.class, BindingType.LIST)));

        JavaResult result = new JavaResult();
        smooks.filterSource(new StreamSource(getClass().getResourceAsStream("input-message-05.csv")), result);

        List<Map> people = (List<Map>) result.getBean("people");
        Map person;

        person = people.get(0);
        assertEquals("TOM", person.get("firstname"));
        assertEquals("fennelly", person.get("lastname"));

        person = people.get(1);
        assertEquals("MIKE", person.get("firstname"));
        assertEquals("fennelly", person.get("lastname"));

        person = people.get(2);
        assertEquals("LINDA", person.get("firstname"));
        assertEquals("coughlan", person.get("lastname"));
    }

    @Test
    public void test_19() throws SmooksException, IOException, SAXException {
        Smooks smooks = new Smooks(getClass().getResourceAsStream("smooks-extended-config-13.xml"));

        ExecutionContext context = smooks.createExecutionContext();
        String result = SmooksUtil.filterAndSerialize(context, getClass().getResourceAsStream("input-message-15.csv"),
                smooks);
        assertEquals(
                "<csv-set><csv-record number=\"1\"><firstname>Tom</firstname><lastname>Collins</lastname><address>Victoria Ave</address><age>32</age></csv-record><csv-record number=\"2\"><firstname>Fred</firstname><lastname>Cook</lastname><address>Mainstreet 12</address><age>40</age></csv-record></csv-set>",
                result);
    }

    @Test
    public void test_20() throws SmooksException, IOException, SAXException {
        Smooks smooks = new Smooks(getClass().getResourceAsStream("smooks-extended-config-14.xml"));

        ExecutionContext context = smooks.createExecutionContext();
        String result = SmooksUtil.filterAndSerialize(context, getClass().getResourceAsStream("input-message-15.csv"),
                smooks);
        assertEquals(
                "<csv-set><csv-record number=\"1\"><firstname>Tom</firstname><lastname>Collins</lastname><address>Victoria Ave</address><age>32</age></csv-record><csv-record number=\"2\"><firstname>Fred</firstname><lastname>Cook</lastname><address>Mainstreet 12</address><age>40</age></csv-record></csv-set>",
                result);
    }

    @Test
    public void test_21() throws SmooksException, IOException, SAXException {
        Smooks smooks = new Smooks(getClass().getResourceAsStream("smooks-extended-config-15.xml"));

        ExecutionContext context = smooks.createExecutionContext();
        String result = SmooksUtil.filterAndSerialize(context, getClass().getResourceAsStream("input-message-16.csv"),
                smooks);
        assertEquals(
                "<csv-set><csv-record number=\"1\"><firstname>Tom</firstname><lastname>Collins</lastname><address>Victoria Ave</address><age>32</age></csv-record><csv-record number=\"2\"><firstname>Fred</firstname><lastname>Cook</lastname><address>Mainstreet 12</address><age>40</age></csv-record></csv-set>",
                result);
    }
   
    @Test 
    public void test_null_field_values() throws SmooksException, IOException, SAXException {
        Smooks smooks = new Smooks();

        smooks.setReaderConfig(new CSVRecordParserConfigurator("firstname,lastname,gender,age,country"));

        StringResult result = new StringResult();
        smooks.filterSource(new StreamSource(getClass().getResourceAsStream("input-message-null-field-values.csv")), result);

        assertEquals(
                "<csv-set><csv-record number=\"1\"><firstname>Tom</firstname><lastname>Fennelly</lastname><gender>Male</gender><age>4</age><country>Ireland</country></csv-record><csv-record number=\"2\"><firstname>Mike</firstname><lastname>Fennelly</lastname><gender>Male</gender><age>2</age><country>Ireland</country></csv-record><csv-record number=\"3\"><firstname>Joel</firstname><lastname>Pearson</lastname><gender>Male</gender><age></age><country>Australia</country></csv-record></csv-set>",
                result.getResult());
    }
}

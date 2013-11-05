package org.cs2c.nginlib.configtest;

import static org.junit.Assert.*;

import java.util.List;

import org.cs2c.nginlib.AuthInfo;
import org.cs2c.nginlib.RecAuthInfo;
import org.cs2c.nginlib.RemoteException;
import org.cs2c.nginlib.config.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class RecConfiguratorTest {

	String midwarePath;
	AuthInfo authInfo= new RecAuthInfo();

	
	RecConfigurator orc = new RecConfigurator(authInfo,midwarePath);
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

	}

	@Before
	public void setUp() throws Exception {
		authInfo.setHost("10.1.50.4");
		authInfo.setUsername("root");
		authInfo.setPassword("cs2csolutions");
	}

	@Test
	public final void testRecConfigurator() {
		RecConfigurator orct = new RecConfigurator(authInfo,midwarePath);
	}

	@Test
	public final void testSetConfpathWithName() {
		String PathWithName = "D:\\eclipseWorkspace\\confpath\\nginx.conf";
		orc.SetConfpathWithName(PathWithName);
	}

	@Test
	public final void testAppend() throws RemoteException {
		String blockName = null;
//		outerBlockNames can be "http:0|server:0"
		String outerBlockNames = "";
		List<Block> list= null;
		
		testSetConfpathWithName();
		
		// case 1:
		
		list= orc.getBlocks(blockName, outerBlockNames);
		assertEquals(1, list.size());
		
		outerBlockNames = "events1";
		orc.append(list.get(0), outerBlockNames);

		outerBlockNames = "";
		list= orc.getBlocks(blockName, outerBlockNames);
		assertEquals(2, list.size());
	}
	
	@Test
	public final void testAppend1() throws RemoteException {
		String blockName = null;
//		outerBlockNames can be "http:0|server:0"
		String outerBlockNames = "";
		List<Block> list= null;
		
		testSetConfpathWithName();
		
		// case 1:
		RecBlock bl = new RecBlock();
		blockName = "events";
		bl.setName(blockName);
		String BlockText="events {"+"\n"
						+"    worker_connections  2000;"+"\n"
						+"}";
		bl.SetBlockText(BlockText);
		
		list= orc.getBlocks(blockName, outerBlockNames);
		assertEquals(1, list.size());
		
		outerBlockNames = "events1";
		orc.append(bl, outerBlockNames);

		outerBlockNames = "";
		list= orc.getBlocks(blockName, outerBlockNames);
		assertEquals(2, list.size());
	}
	@Test
	public final void testAppend2() throws RemoteException {
		String blockName = null;
//		outerBlockNames can be "http:0|server:0"
		String outerBlockNames = "";
		List<Block> list= null;
		
		testSetConfpathWithName();
		
		// case 1:
		RecBlock bl = new RecBlock();
		blockName = "events";
		bl.setName(blockName);
		String BlockText="events {"+"\n"
						+"    worker_connections  2000;"+"\n"
						+"}";
		bl.SetBlockText(BlockText);
		
		list= orc.getBlocks(blockName, outerBlockNames);
		assertEquals(1, list.size());
		
		outerBlockNames = "http:0|server:0";
		orc.append(bl, outerBlockNames);

		outerBlockNames = "";
		list= orc.getBlocks(blockName, outerBlockNames);
		assertEquals(2, list.size());
	}
	
	@Test
	public final void testDelete() throws RemoteException {
		String blockName = null;
//		outerBlockNames can be "http:0|server:0"
		String outerBlockNames = "";
		List<Block> list= null;
		
		testSetConfpathWithName();
		
		// case 1:
		RecBlock bl = new RecBlock();
		blockName = "events";
		bl.setName(blockName);
		String BlockText="events {"+"/n"
						+"    worker_connections  1024;"+"/n"
						+"}";
		bl.SetBlockText(BlockText);
		
		list= orc.getBlocks(blockName, outerBlockNames);
		assertEquals(1, list.size());

		orc.delete(list.get(0), outerBlockNames);

		outerBlockNames = "";
		list= orc.getBlocks(blockName, outerBlockNames);
		assertEquals(0, list.size());

	}

	@Test
	public final void testInsertAfter() throws RemoteException {
		String blockName = null;
//		outerBlockNames can be "http:0|server:0"
		String outerBlockNames = "";
		List<Block> list= null;
		
		testSetConfpathWithName();
		
		// case 1:
		RecBlock bl = new RecBlock();
		blockName = "InsertAfter";
		bl.setName(blockName);
		String BlockText="   InsertAfter {"+"\n"
						+"        InsertAfter worker_connections  2000;"+"\n"
						+"   }";
		bl.SetBlockText(BlockText);
		
		list= orc.getBlocks("events", outerBlockNames);
		
		outerBlockNames = "events1";
		orc.insertAfter(list.get(0), bl, outerBlockNames);

		outerBlockNames = "";
		list= orc.getBlocks("InsertAfter", outerBlockNames);
		assertEquals(1, list.size());
	}

	@Test
	public final void testReplace() throws RemoteException {
		String blockName = null;
//		outerBlockNames can be "http:0|server:0"
		String outerBlockNames = "";
		List<Block> list= null;
		
		testSetConfpathWithName();
		
		// case 1:
		RecBlock bl = new RecBlock();
		blockName = "InsertAfter";
		bl.setName(blockName);
		String BlockText="   InsertAfter {"+"\n"
						+"        InsertAfter worker_connections  2000;"+"\n"
						+"   }"+"\n";
		bl.SetBlockText(BlockText);
		
		list= orc.getBlocks("events", outerBlockNames);
		
		outerBlockNames = "events1";
		orc.replace(list.get(0), bl, outerBlockNames);

		outerBlockNames = "";
		list= orc.getBlocks("InsertAfter", outerBlockNames);
		assertEquals(1, list.size());
	}

	@Test
	public final void testGetBlocks() {
		String blockName = null;
		String outerBlockNames = "";
		List<Block> list= null;
		try {
			testSetConfpathWithName();
			
			// case 1
			blockName = "events";
			list= orc.getBlocks(blockName, outerBlockNames);
			assertEquals(2, list.size());
			assertEquals(blockName, list.get(0).getName());
			
			// case 2
			blockName = "server";
			list= orc.getBlocks(blockName, outerBlockNames);
			assertEquals(1, list.size());
			assertEquals(blockName, list.get(0).getName());
			
			// case 3
			blockName = "location /";
			list= orc.getBlocks(blockName, outerBlockNames);
			assertEquals(2, list.size());
			assertEquals(blockName, list.get(0).getName());
			assertEquals(blockName, list.get(1).getName());
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Test
	public final void testNewBlock() {
		Block op = orc.newBlock();
		
		assertNotNull(op);
		assertTrue(null == op.getName());
		assertTrue(null == op.toString());
	}

	@Test
	public final void testNewDirective() {
		Directive op = orc.newDirective();
		
		assertNotNull(op);
		assertTrue(null == op.getName());
		assertTrue(null == op.toString());
	}

	@Test
	public final void testNewVariable() {
		Variable op = orc.newVariable();
		
		assertNotNull(op);
		assertTrue(null == op.getName());
		assertTrue(null == op.toString());
	}

	@Test
	public final void testNewStringParameter() {
		StringParameter op = orc.newStringParameter();		
		assertNotNull(op);
		assertTrue(null == op.toString());
	}

	@Test
	public final void testNewOption() {
		Option op = orc.newOption();

		assertNotNull(op);
		assertTrue(null == op.getName());
		assertTrue(null == op.toString());
	}

}
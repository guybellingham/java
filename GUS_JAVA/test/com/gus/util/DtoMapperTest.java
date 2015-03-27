package com.gus.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import junit.framework.TestCase;

import org.junit.Test;

import com.gus.test.beans.BusinessBean1;
import com.gus.test.beans.BusinessBean1DTO;
import com.gus.test.beans.BusinessBean2;
import com.gus.test.beans.BusinessBean2DTO;

public class DtoMapperTest extends TestCase {
	/**
	 * map from BusinessBean1 --> BusinessBean1DTO (recursively)
	 * With no BusinessBeanIF nonsense ...who needs an interface on a data bean?
	 */
	public static final	DtoMapper<BusinessBean1,BusinessBean1DTO> DTO_MAPPER = new DtoMapper<BusinessBean1,BusinessBean1DTO>();
	

	public static final TimeZone NEW_YORK = TimeZone.getTimeZone("America/New York");
	public static final TimeZone CHICAGO = TimeZone.getTimeZone("America/Chicago");
	public static final Calendar TODAY1 = Calendar.getInstance(NEW_YORK);
	public static final Calendar TODAY2 = Calendar.getInstance(CHICAGO);
	public static final BigDecimal BIG_DECIMAL = new BigDecimal(2^12L);
	
	public static BusinessBean2 BEAN21 = new BusinessBean2();
	public static BusinessBean2 BEAN22 = new BusinessBean2(CHICAGO,TODAY2,BIG_DECIMAL,"Beaner",false,99,3.95,new String[]{"Sat","Sun"});
	public static List<BusinessBean2> BEAN2_LIST = new ArrayList<BusinessBean2>();
	static {
		BEAN2_LIST.add(BEAN21);
		BEAN2_LIST.add(BEAN22);
	}
	public static BusinessBean1 BEAN11 = new BusinessBean1();
	public static BusinessBean1 BEAN12 = new BusinessBean1(NEW_YORK,TODAY1,BIG_DECIMAL,"Neener",true,33,1.5,new String[]{"Mon","Fri"},BEAN2_LIST);
	
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	@Test
	public void testMapTo() {
		BusinessBean1DTO dto1 = DTO_MAPPER.mapTo(BEAN11, BusinessBean1DTO.class);
		assertNotNull("BusinesBean1DTO is missing?", dto1);
		assertNotNull("BusinesBean2DTO List is missing?", dto1.getBeanList());
		assertNotNull("", dto1.getCalendar());
		assertEquals("", 21356.7, dto1.getDecimalNumber());
		assertEquals("", Boolean.TRUE, dto1.getFlag());
		assertEquals("", "Hello I am BusinessBean1!", dto1.getString());
	}
	@Test
	public void testMapToArray() {
		BusinessBean1[] bean1Array = new BusinessBean1[2];
		bean1Array[0] = BEAN11;
		bean1Array[1] = BEAN12;
		
		BusinessBean1DTO[] dto1Array = DTO_MAPPER.mapTo(bean1Array, BusinessBean1DTO.class);
		assertNotNull("BusinesBean1DTO Array is missing?", dto1Array);
		assertNotNull("BusinesBean1DTO[0] is missing?", dto1Array[0]);
		assertNotNull("BusinesBean1DTO[1] is missing?", dto1Array[1]);
		BusinessBean1DTO dto2 = dto1Array[1];
		assertNotNull("", dto2.getBeanList());
		assertEquals("",2, dto2.getBeanList().size());
		assertTrue("", dto2.getBeanList().get(0) instanceof BusinessBean2DTO);
	}

}

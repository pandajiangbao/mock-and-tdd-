package sales;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SalesAppTest {

    @Mock
    Sales sales;

    @Mock
    SalesDao salesDao;

    @Mock
    SalesReportData salesReportData1;

    @Mock
    SalesReportData salesReportData2;

    @Mock
    SalesReportDao salesReportDao;

    @Mock
    EcmService ecmService;

    @Spy
    @InjectMocks
    SalesApp salesApp = new SalesApp();

    public static final int DAY = 24 * 60 * 60 * 1000;


    private static List<String> isNatTradeHeader = Arrays.asList("Sales ID", "Sales Name", "Activity", "Time");
    private static List<String> notNatTradeHeader = Arrays.asList("Sales ID", "Sales Name", "Activity", "Local Time");

    @Test
    public void test_checkDate_should_return_false_when_checkDate_valid() {
        // given
        when(sales.getEffectiveTo()).thenReturn(new Date(System.currentTimeMillis() + DAY));
        when(sales.getEffectiveFrom()).thenReturn(new Date(System.currentTimeMillis() - DAY));

        // when
        boolean result = salesApp.checkDate(sales);

        // then
        Assert.assertFalse(result);
    }

    @Test
    public void test_checkDate_should_return_true_when_checkDate_invalid() {
        // given
        when(sales.getEffectiveTo()).thenReturn(new Date(System.currentTimeMillis() - DAY));
        when(sales.getEffectiveFrom()).thenReturn(new Date(System.currentTimeMillis() - 2 * DAY));

        // when
        boolean result = salesApp.checkDate(sales);

        // then
        Assert.assertTrue(result);
    }

    @Test
    public void test_getHeader_should_return_isNatTradeHeaders_when_isNatTrade_true() {
        // given
        boolean isNatTrade = true;

        //when
        List<String> header = salesApp.getHeader(isNatTrade);

        //then
        Assert.assertEquals(isNatTradeHeader, header);
    }

    @Test
    public void test_getHeader_should_return_isNatTradeHeaders_when_isNatTrade_false() {
        // given
        boolean isNatTrade = false;

        //when
        List<String> header = salesApp.getHeader(isNatTrade);

        //then
        Assert.assertEquals(notNatTradeHeader, header);
    }

    @Test
    public void test_filteredReportData_should_return_filteredReportDataList_size_2_when_isSupervisor_true_and_one_isConfidential_true_another_isConfidential_false() {
        // given
        boolean isSupervisor = true;

        when(salesReportData1.getType()).thenReturn("SalesActivity");
        when(salesReportData1.isConfidential()).thenReturn(true);

        when(salesReportData2.getType()).thenReturn("SalesActivity");
        when(salesReportData2.isConfidential()).thenReturn(false);

        List<SalesReportData> reportDataList = Arrays.asList(salesReportData1, salesReportData2);

        //when
        List<SalesReportData> filteredReportDataList = salesApp.filteredReportData(isSupervisor, reportDataList);

        //then
        Assert.assertEquals(2, filteredReportDataList.size());
    }

    @Test
    public void test_filteredReportData_should_return_filteredReportDataList_size_1_when_isSupervisor_false_and_one_isConfidential_true_another_isConfidential_false() {
        // given
        boolean isSupervisor = false;

        when(salesReportData1.getType()).thenReturn("SalesActivity");
        when(salesReportData1.isConfidential()).thenReturn(true);

        when(salesReportData2.getType()).thenReturn("SalesActivity");
        when(salesReportData2.isConfidential()).thenReturn(false);

        List<SalesReportData> reportDataList = Arrays.asList(salesReportData1, salesReportData2);

        //when
        List<SalesReportData> filteredReportDataList = salesApp.filteredReportData(isSupervisor, reportDataList);

        //then
        Assert.assertEquals(1, filteredReportDataList.size());
    }

    @Test
    public void test_getLimitList_should_return_filteredReportDataList_size_3_when_maxRow_3_and_filteredReportDataList_size_4() {
        // given
        int maxRow = 3;
        List<SalesReportData> reportDataList = Arrays.asList(salesReportData1, salesReportData2,salesReportData1, salesReportData2);

        //when
        List<SalesReportData> filteredReportDataList = salesApp.getLimitList(maxRow, reportDataList);

        //then
        Assert.assertEquals(3, filteredReportDataList.size());
    }

    @Test
    public void test_getLimitList_should_return_filteredReportDataList_size_4_when_maxRow_5_and_filteredReportDataList_size_4() {
        // given
        int maxRow = 5;
        List<SalesReportData> reportDataList = Arrays.asList(salesReportData1, salesReportData2,salesReportData1, salesReportData2);

        //when
        List<SalesReportData> filteredReportDataList = salesApp.getLimitList(maxRow, reportDataList);

        //then
        Assert.assertEquals(4, filteredReportDataList.size());
    }

    @Test
    public void test_generateSalesActivityReport() {
        // given
        SalesActivityReport report = mock(SalesActivityReport.class);
        when(report.toXml()).thenReturn("panda");

        when(sales.getEffectiveTo()).thenReturn(new Date(System.currentTimeMillis() + DAY));
        when(sales.getEffectiveFrom()).thenReturn(new Date(System.currentTimeMillis() - DAY));
        when(salesDao.getSalesBySalesId(anyString())).thenReturn(sales);

        when(salesReportData1.getType()).thenReturn("SalesActivity");
        when(salesReportData1.isConfidential()).thenReturn(true);
        when(salesReportData2.getType()).thenReturn("SalesActivity");
        when(salesReportData2.isConfidential()).thenReturn(false);

        when(salesReportDao.getReportData(any())).thenReturn(Arrays.asList(salesReportData1,salesReportData2));

        doReturn(report).when(salesApp).generateReport(anyListOf(String.class), anyListOf(SalesReportData.class));
        //when
        salesApp.generateSalesActivityReport("DUMMY", 1000, false, false);

        //then
        verify(salesApp).generateReport(eq(notNatTradeHeader), anyListOf(SalesReportData.class));
        verify(ecmService).uploadDocument("panda");
    }
}

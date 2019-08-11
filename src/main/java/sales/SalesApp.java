package sales;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class SalesApp {

    private SalesDao salesDao = new SalesDao();

    private SalesReportDao salesReportDao = new SalesReportDao();

    private EcmService ecmService = new EcmService();

    public void generateSalesActivityReport(String salesId, int maxRow, boolean isNatTrade, boolean isSupervisor) {

        if (salesId == null) {
            return;
        }

        Sales sales = salesDao.getSalesBySalesId(salesId);

        if (sales == null || checkDate(sales)) return;

        List<SalesReportData> reportDataList = salesReportDao.getReportData(sales);
        //过滤数据
        List<SalesReportData> filteredReportDataList = filteredReportData(isSupervisor, reportDataList);
        //限制行数
        filteredReportDataList = getLimitList(maxRow, filteredReportDataList);

        List<String> headers = getHeader(isNatTrade);

        SalesActivityReport report = this.generateReport(headers, filteredReportDataList);

        ecmService.uploadDocument(report.toXml());

    }

    List<SalesReportData> getLimitList(int maxRow, List<SalesReportData> filteredReportDataList) {
        return filteredReportDataList.stream()
                .limit(Math.min(maxRow, filteredReportDataList.size()))
                .collect(Collectors.toList());
    }

    List<SalesReportData> filteredReportData(boolean isSupervisor, List<SalesReportData> reportDataList) {
        List<SalesReportData> filteredReportDataList = new ArrayList<>();

        for (SalesReportData data : reportDataList) {
            if ("SalesActivity".equalsIgnoreCase(data.getType())) {
                if (data.isConfidential()) {
                    if (isSupervisor) {
                        filteredReportDataList.add(data);
                    }
                } else {
                    filteredReportDataList.add(data);
                }
            }
        }
        return filteredReportDataList;
    }

    List<String> getHeader(boolean isNatTrade) {
        if (isNatTrade) {
            return Arrays.asList("Sales ID", "Sales Name", "Activity", "Time");
        }
        return Arrays.asList("Sales ID", "Sales Name", "Activity", "Local Time");

    }

    boolean checkDate(Sales sales) {
        Date today = new Date();
        return today.after(sales.getEffectiveTo())
                || today.before(sales.getEffectiveFrom());
    }

    SalesActivityReport generateReport(List<String> headers, List<SalesReportData> reportDataList) {
        // TODO Auto-generated method stub
        return null;
    }

}

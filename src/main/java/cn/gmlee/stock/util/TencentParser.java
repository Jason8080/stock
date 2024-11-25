package cn.gmlee.stock.util;

import cn.gmlee.stock.mod.Stock;
import cn.gmlee.tools.base.util.BoolUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 实时行情: https://qt.gtimg.cn/q=sz300059,sz002008
 * v_sz300059="51~东方财富~300059~25.63~27.41~27.34~11396456~5312319~6084137~25.63~68713~25.62~25790~25.61~7568~25.60~18653~25.59~4616~25.64~2321~25.65~5152~25.66~3310~25.67~1843~25.68~6329~~20241115161439~-1.78~-6.49~27.55~25.59~25.63/11396456/30195067880~11396456~3019507~8.53~50.41~~27.55~25.59~7.15~3423.98~4045.83~5.26~32.89~21.93~1.02~106385~26.50~50.22~49.38~~~1.95~3019506.7880~782.8607~3054~ A~GP-A-CYB~83.07~-9.24~0.16~10.43~2.46~31.00~9.87~11.43~11.68~140.66~13359278683~15785542475~73.73~92.71~13359278683~~~68.07~-0.50~~CNY~0~~25.58~5377"; v_sz002008="51~大族激光~002008~28.20~26.68~26.59~1315257~675234~640023~28.20~2940~28.19~476~28.18~2663~28.17~109~28.16~111~28.21~249~28.22~621~28.23~670~28.24~418~28.25~974~~20241115161403~1.52~5.70~29.35~26.48~28.20/1315257/3742572101~1315257~374257~13.43~18.43~~29.35~26.48~10.76~276.16~296.72~1.87~29.35~24.01~2.19~3367~28.46~15.60~36.18~~~1.42~374257.2101~0.0000~0~ ~GP-A~37.41~3.45~0.70~10.13~4.99~29.35~14.25~12.85~17.60~40.79~979283962~1052193000~36.47~57.61~979283962~~~25.21~-0.28~~CNY~0~~28.30~-1248";
 * 历史行情: https://proxy.finance.qq.com/ifzqgtimg/appstock/app/newfqkline/get?param=sz300059,day,2024-10-10,2024-10-10,1,qfq
 * {"code":0,"msg":"","data":{"sz300059":{"qfqday":[["2024-10-10","24.10","20.55","24.56","19.92","23863267.00",{},"17.86","5197314.06",""]],"qt":{"sz300059":["51","\u4e1c\u65b9\u8d22\u5bcc","300059","27.39","26.67","26.50","9635016","5211844","4423172","27.38","6943","27.37","1918","27.36","1574","27.35","2431","27.34","857","27.39","4284","27.40","9942","27.41","6915","27.42","6925","27.43","2970","","20241121161439","0.72","2.70","27.95","26.46","27.39\/9635016\/26191780553","9635016","2619178","7.21","53.87","","27.95","26.46","5.59","3659.11","4323.66","5.62","32.00","21.34","1.05","-17313","27.18","53.67","52.77","","","1.97","2619178.0553","325.9410","1190"," A","GP-A-CYB","95.64","-0.07","0.15","10.43","2.46","31.00","9.87","-8.70","23.43","160.36","13359278683","15785542475","-38.68","102.74","13359278683","","","83.09","-0.07","","CNY","0","","27.30","7424"],"market":["2024-11-21 18:21:25|HK_close_\u5df2\u6536\u76d8|SH_close_\u5df2\u6536\u76d8|SZ_close_\u5df2\u6536\u76d8|US_close_\u672a\u5f00\u76d8|SQ_close_\u5df2\u4f11\u5e02|DS_close_\u5df2\u4f11\u5e02|ZS_close_\u5df2\u4f11\u5e02|NEWSH_close_\u5df2\u6536\u76d8|NEWSZ_close_\u5df2\u6536\u76d8|NEWHK_close_\u5df2\u6536\u76d8|NEWUS_close_\u672a\u5f00\u76d8|REPO_close_\u5df2\u6536\u76d8|UK_open_\u4ea4\u6613\u4e2d|KCB_close_\u5df2\u6536\u76d8|IT_open_\u4ea4\u6613\u4e2d|MY_close_\u5df2\u6536\u76d8|EU_open_\u4ea4\u6613\u4e2d|AH_close_\u5df2\u6536\u76d8|DE_open_\u4ea4\u6613\u4e2d|JW_close_\u5df2\u6536\u76d8|CYB_close_\u5df2\u6536\u76d8|USA_close_\u672a\u5f00\u76d8|USB_open_\u76d8\u524d\u4ea4\u6613|ZQ_close_\u5df2\u6536\u76d8"],"zjlx":["sz300059","0.00","0.00","0.00","0","0.00","0.00","0.00","0","0.00","0.00","0.00","\u4e1c\u65b9\u8d22\u5bcc","20200701","20200701^0.00^0.00","20200630^0.00^0.00","20200629^0.00^0.00","20200624^0.00^0.00","0.00","0.00","20200701085013"]},"mx_price":{"mx":{"data":[],"timeline":[]},"price":{"data":[]}},"attribute":{"NoProfit":"0","WeightedVotingRights":"1","IsVIE":"0","IsRegistration":"0"},"prec":"24.90","fsStartDate":"20201009","version":"18"}}}
 */
public class TencentParser {
    public static List<Stock> parse(String response) {
        List<Stock> stockList = new ArrayList<>();
        String[] entries = response.split(";");
        for (String entry : entries) {
            if (!entry.contains("=")) continue;

            String data = entry.split("=")[1].replace("\"", "");
            String[] fields = data.split("~");

            if(fields.length < 87){
                continue;
            }

//            for(int i=0; i<fields.length; i++){
//                System.out.println(String.format("[%s]: %s", i, fields[i]));
//            }

            Stock stock = new Stock();
            stock.setName(fields[1]); // 名称
            stock.setCode(fields[2]); // 代码
            stock.setCurrentPrice(fields[3]); // 价格
            stock.setPreviousClose(fields[4]); // 昨收
            stock.setOpenPrice(fields[5]); // 今开
            stock.setHighestPrice(fields[41]); // 最高价
            stock.setLowestPrice(fields[42]); // 最低价
            stock.setAvgPrice(fields[51]); // 均价
            stock.setRiseRatio(fields[32]); // 涨幅
            stock.setAmplitude(fields[43]); // 振幅
            stock.setUpperPrice(fields[47]); // 涨停价
            stock.setLowerPrice(fields[48]);// 跌停价
            stock.setVolumeRatio(fields[49]); // 量比
            stock.setTurnoverRate(fields[38]);// 换手率
            stock.setRiseRate(fields[83]); // 涨速
            stock.setVolume(fields[36]); // 成量 (手)
            stock.setTurnover(fields[37]); // 成额 (万)
            stock.setSellVolume(fields[8]); // 卖盘 (元)
            stock.setBuyVolume(fields[7]); // 买盘 (元)
            stock.setTcRatio(fields[74]); // 委比
            stock.setPeRatio(fields[52]); // 市盈(动)
            stock.setPbRatio(fields[46]); // 市净
            stock.setDyRatio(fields[64]); // 股息
            stock.setMcTotal(fields[45]); // 市值
            stock.setMcCirculate(fields[44]); // 流通
            stock.setTopYear(fields[67]); // 年顶
            stock.setBottomYear(fields[68]); // 年底
            stock.setTimestamp(fields[30]); // 时间

            stockList.add(stock);
        }
        return stockList;
    }
}

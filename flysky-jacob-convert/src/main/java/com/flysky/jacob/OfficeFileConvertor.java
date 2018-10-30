package com.flysky.jacob;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Office文档转换器
 *
 * @author xiaoh
 * @create 2018-10-30 10:44
 **/
public class OfficeFileConvertor {
    private final static Logger logger = LoggerFactory.getLogger(OfficeFileConvertor.class);

    private static final Integer WORD_TO_PDF_OPERAND = 17;
    private static final Integer PPT_TO_PDF_OPERAND = 32;
    private static final Integer EXCEL_TO_PDF_OPERAND = 0;

    /**
     * word(.doc/.docx)文档转换为pdf文档
     * @param srcFilePath word文档路径
     * @param pdfFilePath pdf文档路径
     */
    public static void doc2pdf(String srcFilePath, String pdfFilePath) {
        logger.info("启动Word转换 ...");
        long start = System.currentTimeMillis();
        ActiveXComponent app = null;
        Dispatch doc = null;
        try {
            ComThread.InitSTA();
            app = new ActiveXComponent("Word.Application");
            app.setProperty("Visible", false);
            Dispatch docs = app.getProperty("Documents").toDispatch();
            Object[] obj = new Object[]{
                    srcFilePath,
                    new Variant(false),
                    new Variant(false),//是否只读
                    new Variant(false),
                    new Variant("pwd")
            };
            doc = Dispatch.invoke(docs, "Open", Dispatch.Method, obj, new int[1]).toDispatch();
//          Dispatch.put(doc, "Compatibility", false);  //兼容性检查,为特定值false不正确
            Dispatch.put(doc, "RemovePersonalInformation", false);

            // 文件检测
            detectiveDestFile(pdfFilePath);

            Dispatch.call(doc, "ExportAsFixedFormat", pdfFilePath, WORD_TO_PDF_OPERAND); // word保存为pdf格式宏，值为17

            long end = System.currentTimeMillis();
            logger.info("转换完成.用时：" + (end - start) + "ms.");

        }catch (Exception e) {
            e.printStackTrace();
            logger.error("========Error:WORD文档转换失败：" + e.getMessage());
        } finally {
            if (doc != null) {
                logger.info("关闭文档");
                Dispatch.call(doc, "Close", false);
            }
            if (app != null) {
                app.invoke("Quit", 0);
            }
            // 如果没有这句话,winword.exe进程将不会关闭
            ComThread.Release();
        }
    }

    /**
     * PPT(.ppt/.pptx)文档转换为PDF
     * @param srcFilePath ppt文档地址
     * @param pdfFilePath pdf文档地址
     */
    public static void ppt2pdf(String srcFilePath, String pdfFilePath) {
        logger.info("启动PPT转换 ...");
        long start = System.currentTimeMillis();
        ActiveXComponent app = null;
        Dispatch ppt = null;
        try {
            ComThread.InitSTA();
            app = new ActiveXComponent("PowerPoint.Application");
            Dispatch ppts = app.getProperty("Presentations").toDispatch();

            /*
             * call
             * param 4: ReadOnly
             * param 5: Untitled指定文件是否有标题
             * param 6: WithWindow指定文件是否可见
             * */
            ppt = Dispatch.call(ppts, "Open", srcFilePath, true,true, false).toDispatch();

            // 文件检测
            detectiveDestFile(pdfFilePath);

            Dispatch.call(ppt, "SaveAs", pdfFilePath, PPT_TO_PDF_OPERAND); // ppSaveAsPDF为特定值32

            long end = System.currentTimeMillis();
            logger.info("转换完成.用时：" + (end - start) + "ms.");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("========Error:PPT文档转换失败：" + e.getMessage());
        } finally {
            if (ppt != null) {
                logger.info("关闭文档");
                Dispatch.call(ppt, "Close");
            }
            if (app != null) {
                app.invoke("Quit");
            }
            // 如果没有这句话,winword.exe进程将不会关闭
            ComThread.Release();
        }
    }

    /**
     * Excel(.xls/.xlsx)文档转换为PDF
     * @param inFilePath excel文档地址
     * @param outFilePath pdf文档地址
     */
    public static void excel2Pdf(String inFilePath, String outFilePath) {
        logger.info("启动Excel转换 ...");
        long start = System.currentTimeMillis();

        ActiveXComponent ax = null;
        Dispatch excel = null;
        try {
            ComThread.InitSTA();
            ax = new ActiveXComponent("Excel.Application");
            ax.setProperty("Visible", new Variant(false));
            ax.setProperty("AutomationSecurity", new Variant(3)); // 禁用宏
            Dispatch excels = ax.getProperty("Workbooks").toDispatch();

            Object[] obj = new Object[]{
                    inFilePath,
                    new Variant(false),
                    new Variant(false)
            };
            excel = Dispatch.invoke(excels, "Open", Dispatch.Method, obj, new int[9]).toDispatch();

            // 文件检测
            detectiveDestFile(outFilePath);

            // 转换格式
            Object[] obj2 = new Object[]{
                    new Variant(EXCEL_TO_PDF_OPERAND), // PDF格式=0
                    outFilePath,
                    new Variant(0)  //0=标准 (生成的PDF图片不会变模糊) ; 1=最小文件
            };
            Dispatch.invoke(excel, "ExportAsFixedFormat", Dispatch.Method, obj2, new int[1]);

            long end = System.currentTimeMillis();
            logger.info("转换完成.用时：" + (end - start) + "ms.");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("========Error:EXCEL文档转换失败：" + e.getMessage());
        } finally {
            if (excel != null) {
                logger.info("关闭文档");
                Dispatch.call(excel, "Close", new Variant(false));
            }
            if (ax != null) {
                ax.invoke("Quit", new Variant[] {});
                ax = null;
            }
            // 如果没有这句话,winword.exe进程将不会关闭
            ComThread.Release();
        }
    }

    /**
     * 检测目标文件是否存在，如果存在，则删除。
     * @param destFile
     */
    private static void detectiveDestFile(String destFile) {
        // 文件检测
        File tofile = new File(destFile);
        if (tofile.exists()) {
            logger.info("删除存在的已转换文件！");
            tofile.delete();
        }
    }

    public static void main(String[] args) {
        String path = "E:\\";
        doc2pdf(path + "bizware安装手册.docx", path + "bizware安装手册.pdf");
        ppt2pdf(path + "ELK简介.pptx", path + "ELK简介.pdf");
        excel2Pdf(path + "和聚宝测试环境信息.xlsx", path + "和聚宝测试环境信息.pdf");
    }

}

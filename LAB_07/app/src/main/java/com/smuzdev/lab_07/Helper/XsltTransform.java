package com.smuzdev.lab_07.Helper;

import android.app.Application;
import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class XsltTransform extends Application {
    private static final String TAG = "LAB8_DEBUG" ;

    File File_XSLT_TEMPLETE = new File(Environment.getExternalStorageDirectory(), "XSLT_TEMPLATE.xml");
    File File_XML_MARKUP;

    public XsltTransform(File file) {
        File_XML_MARKUP = file;
    }

    String XSLT_TEMPLATE = "" +
            "<?xml version=\"1.0\"?>\n" +
            "\n" +
            "<xsl:stylesheet version=\"1.0\"\n" +
            "xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\n" +
            "\n" +
            "<xsl:template match=\"/\">\n" +
            "  <html>\n" +
            "  <body>\n" +
            "    <h2>My Notes</h2>\n" +
            "    <table border=\"1\">\n" +
            "      <tr bgcolor=\"#9acd32\">\n" +
            "        <th>Category</th>\n" +
            "        <th>Title</th>\n" +
            "        <th>Note</th>\n" +
            "        <th>Date</th>\n" +
            "      </tr>\n" +
            "      <xsl:for-each select=\"com.example.lab__8.DataModels.Notes/notesArrayList/com.example.lab__8.DataModels.Note\">\n" +
            "        <tr>\n" +
            "          <td><xsl:value-of select=\"category\"/></td>\n" +
            "          <td><xsl:value-of select=\"title\"/></td>\n" +
            "          <td><xsl:value-of select=\"body\"/></td>\n" +
            "          <td><xsl:value-of select=\"date\"/></td>\n" +
            "        </tr>\n" +
            "      </xsl:for-each>\n" +
            "    </table>\n" +
            "  </body>\n" +
            "  </html>\n" +
            "</xsl:template>\n" +
            "\n" +
            "</xsl:stylesheet>";

    public void Transformation() throws TransformerException, IOException {

        //write to file XSLT TEMPLATE
        FileWriter fw_XSLT = new FileWriter(File_XSLT_TEMPLETE, false);
        fw_XSLT.write(XSLT_TEMPLATE);
        fw_XSLT.close();

        //transform xml with xslt template to file RESULT.html
        TransformerFactory factory = TransformerFactory.newInstance();
        Source xslt = new StreamSource(File_XSLT_TEMPLETE);
        Transformer transformer = factory.newTransformer(xslt);
        Source xml = new StreamSource(File_XML_MARKUP);
        transformer.transform(xml, new StreamResult(new File(Environment.getExternalStorageDirectory(), "RESULT.html")));
    }
}

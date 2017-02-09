import lt.compiler.*;
import lt.compiler.semantic.STypeDef;
import lt.compiler.syntactic.Statement;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by wkgcass on 16/6/4.
 */
public class SimpleTest {
        public static void main(String[] args) throws Exception {
                ErrorManager err = new ErrorManager(true);

                StringBuilder sb = new StringBuilder();
                sb.append("" +
                        "class TestListCastInvokeMethod\n" +
                        "    static\n" +
                        "        def method=Container(10, [1,2,3])\n" +
                        "data class Bean\n" +
                        "    public list = []\n" +
                        "    def add(o)=list.add(o)\n" +
                        "data class Container(i, x:Bean)"
                );

                lt.compiler.Scanner lexicalProcessor = new lt.compiler.IndentScanner("test.lt", new StringReader(sb.toString()), new Properties(), err);
                Parser syntacticProcessor = new Parser(lexicalProcessor.scan(), err);
                Map<String, List<Statement>> map = new HashMap<String, List<Statement>>();
                map.put("test.lt", syntacticProcessor.parse());
                SemanticProcessor semanticProcessor = new SemanticProcessor(map, Thread.currentThread().getContextClassLoader(), err);
                Set<STypeDef> types = semanticProcessor.parse();

                CodeGenerator codeGenerator = new CodeGenerator(types, semanticProcessor.getTypes());
                Map<String, byte[]> list = codeGenerator.generate();
                byte[] b = list.get("TestListCastInvokeMethod");
                FileOutputStream fos = new FileOutputStream(new File("/Volumes/PROJECTS/openSource/LessTyping/hehe.class"));
                fos.write(b);
                fos.flush();
                fos.close();
        }

        public static boolean test() {
                Object a = new Object();
                Object b = new Object();
                return a == b;
        }
}

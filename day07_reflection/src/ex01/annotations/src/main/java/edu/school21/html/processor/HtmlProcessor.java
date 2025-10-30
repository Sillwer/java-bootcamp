package edu.school21.html.processor;

import com.google.auto.service.AutoService;
import edu.school21.html.annotation.HtmlForm;
import edu.school21.html.annotation.HtmlInput;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
@SupportedAnnotationTypes("edu.school21.html.annotation.*")
public class HtmlProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations.isEmpty()) {
            return false;
        }

        for (Element formElement : roundEnv.getElementsAnnotatedWith(HtmlForm.class)) {
            HtmlForm htmlForm = formElement.getAnnotation(HtmlForm.class);
            try {
                FileObject builderFile = processingEnv.getFiler().createResource(StandardLocation.SOURCE_OUTPUT, "", htmlForm.fileName());
                PrintWriter out = new PrintWriter(builderFile.openWriter());

                out.format("<form action = \"%s\" method = \"%s\">\n", htmlForm.action(), htmlForm.method());

                List<Element> fieldElements = formElement.getEnclosedElements()
                        .stream()
                        .map(e -> (Element) e)
                        .filter(e -> e.getKind().isField())
                        .collect(Collectors.toList());

                for (Element fe : fieldElements) {
                    HtmlInput htmlInput = fe.getAnnotation(HtmlInput.class);
                    out.printf("\t<input type = \"%s\" name = \"%s\" placeholder = \"%s\">\n",
                            htmlInput.type(), htmlInput.name(), htmlInput.placeholder());
                }

                out.println("\t<input type = \"submit\" value = \"Send\">");
                out.print("</form>");

                out.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return true;
    }

    void printlnWarningMsg(String msg) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, msg);
    }
}

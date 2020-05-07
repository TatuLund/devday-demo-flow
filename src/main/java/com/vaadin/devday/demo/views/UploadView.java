package com.vaadin.devday.demo.views;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.io.IOUtils;

import com.vaadin.devday.demo.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HtmlComponent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.internal.MessageDigestUtil;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

import elemental.json.Json;
import elemental.json.JsonArray;

@Route(value = UploadView.ROUTE, layout = MainLayout.class)
@PageTitle(UploadView.TITLE)
public class UploadView extends VerticalLayout {
    public static final String ROUTE = "upload";
    public static final String TITLE = "Upload";
    
    public UploadView() {
        Div output = new Div();
        
    	MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
    	// Button is removed with style module, see styles.html
    	Upload upload = new Upload(buffer);
    	upload.setSizeFull();
    	upload.addStartedListener(event -> {
    		upload.getElement().getPropertyNames().forEach(prop -> System.out.println(prop+" "+upload.getElement().getProperty(prop)));
    	});
    	upload.addFileRejectedListener(event -> {
    		Notification.show(event.getErrorMessage());
    	});
    	upload.setAcceptedFileTypes("image/jpeg");
    	
    	upload.addSucceededListener(event -> {
    	    Component component = createComponent(event.getMIMEType(),
    	            event.getFileName(),
    	            buffer.getInputStream(event.getFileName()));
    	    showOutput(event.getFileName(), component, output);
        	remove(upload);
    	}); 
    	
    	upload.addFailedListener(event -> {
    		Notification.show("Failed to load file: "+event.getFileName()).addThemeVariants(NotificationVariant.LUMO_ERROR);
    		upload.getElement().setPropertyJson("files", Json.createArray());
    	});

    	add(upload,output);
    }

    
    private Component createComponent(String mimeType, String fileName,
            InputStream stream) {
        if (mimeType.startsWith("text")) {
            String text = "";
            try {
                text = IOUtils.toString(stream, "UTF-8");
            } catch (IOException e) {
                text = "exception reading stream";
            }
            return new Text(text);
        } else if (mimeType.startsWith("image")) {
            Image image = new Image();
            try {

                byte[] bytes = IOUtils.toByteArray(stream);
                image.getElement().setAttribute("src", new StreamResource(
                        fileName, () -> new ByteArrayInputStream(bytes)));
                try (ImageInputStream in = ImageIO.createImageInputStream(
                        new ByteArrayInputStream(bytes))) {
                    final Iterator<ImageReader> readers = ImageIO
                            .getImageReaders(in);
                    if (readers.hasNext()) {
                        ImageReader reader = readers.next();
                        try {
                            reader.setInput(in);
                            image.setWidth(reader.getWidth(0) + "px");
                            image.setHeight(reader.getHeight(0) + "px");
                        } finally {
                            reader.dispose();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return image;
        }
        Div content = new Div();
        String text = String.format("Mime type: '%s'\nSHA-256 hash: '%s'",
                mimeType, MessageDigestUtil.sha256(stream.toString()));
        content.setText(text);
        return new Div();
    }
    
    private void showOutput(String text, Component content,
            HasComponents outputContainer) {
        HtmlComponent p = new HtmlComponent(Tag.P);
        p.getElement().setText(text);
        outputContainer.add(p);
        outputContainer.add(content);
    }
}

package com.yidongwork.test;

import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.geoserver.rest.GeoServerRESTReader;
import it.geosolutions.geoserver.rest.decoder.RESTFeatureType;
import it.geosolutions.geoserver.rest.decoder.RESTFeatureType.Attribute;
import it.geosolutions.geoserver.rest.decoder.RESTLayer;

import it.geosolutions.geoserver.rest.decoder.RESTStyleList;
import it.geosolutions.geoserver.rest.decoder.utils.JDOMBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.Consumes;

import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.FormDataParam;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("myresource")
public class MyResource {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
    	return "get it";
    }
    
    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @POST
    @Path ("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)
    public String upload(@FormDataParam("file") InputStream uploadedInputStream) {
    	System.out.println("start upload");
    	//byte[] b = new byte[10];
    	try {
			//uploadedInputStream.read(b);
			OutputStream outputStream = null;
			outputStream = new FileOutputStream(new File("C:\\Users\\Yang\\Desktop\\shp.zip"));
			
			int read = 0;
			byte[] bytes = new byte[1024];
	 
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
	 
			outputStream.close();
			System.out.println("Done!");
			
			return uploadFile("C:\\Users\\Yang\\Desktop\\shp.zip","shp");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	//Map<String, List<FormDataBodyPart>> maps=form.getFields();
        return "upload function";
    }
    
    public String uploadFile(String path, String filename)
	{
		
		try {
			GeoServerRESTPublisher publisher=new GeoServerRESTPublisher("http://localhost:8082/geoserver", "admin", "geoserver");
			GeoServerRESTReader reader=new GeoServerRESTReader("http://localhost:8082/geoserver", "admin", "geoserver");
			File f=new File(path);
			System.out.println(f.length());
			String str="EPSG:4326";
			boolean result = publisher.publishShp("cite", "testStore", filename, f,str);
			System.out.println(result);
			RESTLayer layer=reader.getLayer("cite", filename);
			System.out.println(layer.getResourceUrl());
			
			String resultStr="http://localhost:8082/geoserver/cite/wms";
			return layer.getResourceUrl();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		
		
		
		
	}
    
    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Path ("/upload2")
    @Produces(MediaType.TEXT_PLAIN)
    public String upload2() {
        return "upload";
    }
}

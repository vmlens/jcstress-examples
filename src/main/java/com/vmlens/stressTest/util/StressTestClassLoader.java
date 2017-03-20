package com.vmlens.stressTest.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

/**
 * 
 * 
 * A class loader which loads a class without looking at classes loaded from a parent class loader.
 * 
 * @author thomas
 *
 */


public class StressTestClassLoader extends ClassLoader {
	
	private final ClassLoader classLoaderForResources;
	
	
	
    public StressTestClassLoader(ClassLoader classLoaderForResources) {
		super(null);
		this.classLoaderForResources = classLoaderForResources;
	}




	protected Class<?> findClass(final String name)
            throws ClassNotFoundException
        {
		     
		 
		 try {
		  
		
		     String resourceName = name.replace('.', '/') + ".class";
		     
		     
		      InputStream stream = StressTestClassLoader.class.getClassLoader().getResourceAsStream(resourceName);
		      byte[] array = IOUtils.toByteArray(stream);
		      
		     return defineClass( name,array, 0 , array.length);
		      
			} catch (IOException e) {
					e.printStackTrace();
					throw new ClassNotFoundException("Exception during load", e);
			}
        }
	
	
	
	
	
	
	

}

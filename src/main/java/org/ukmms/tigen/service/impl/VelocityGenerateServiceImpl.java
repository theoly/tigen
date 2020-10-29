package org.ukmms.tigen.service.impl;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.loader.StringResourceLoader;
import org.apache.velocity.runtime.resource.util.StringResourceRepository;
import org.ukmms.tigen.service.GenerateService;

import java.io.StringWriter;
import java.util.Map;

/**
 * @author theoly
 * @date 2020/10/29
 */
public class VelocityGenerateServiceImpl implements GenerateService {
    @Override
    public String generate(String template, Map<String, Object> info) {
        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.setProperty(Velocity.RESOURCE_LOADERS, "string");
        velocityEngine.setProperty("resource.loader.string.class", StringResourceLoader.class.getName());
        velocityEngine.init();

        VelocityContext context = new VelocityContext();

        context.put( "name", new String("Velocity") );
        Template velocityTempl = null;

        try
        {
            StringResourceRepository repository = StringResourceLoader.getRepository();
            repository.putStringResource("velocity", template);
            velocityTempl = velocityEngine.getTemplate("velocity");
        }
        catch( ResourceNotFoundException rnfe )
        {
            // couldn't find the template
        }
        catch( ParseErrorException pee )
        {
            // syntax error: problem parsing the template
        }
        catch( MethodInvocationException mie )
        {
            // something invoked in the template
            // threw an exception
        }
        catch( Exception e )
        {}

        StringWriter sw = new StringWriter();

        velocityTempl.merge( context, sw );
        return sw.toString();
    }
}

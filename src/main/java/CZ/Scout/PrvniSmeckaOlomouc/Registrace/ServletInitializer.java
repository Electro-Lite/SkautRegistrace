package CZ.Scout.PrvniSmeckaOlomouc.Registrace;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Configures the application when deployed as a WAR to a servlet container.
 */
public class ServletInitializer extends SpringBootServletInitializer {

	/**
     * Configures the application builder to use the main application class.
     *
     * @param application the Spring application builder
     * @return the configured application builder
     */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(RegistraceApplication.class);
	}

}

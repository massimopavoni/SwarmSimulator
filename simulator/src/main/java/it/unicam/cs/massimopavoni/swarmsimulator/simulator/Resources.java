package it.unicam.cs.massimopavoni.swarmsimulator.simulator;

import it.unicam.cs.massimopavoni.swarmsimulator.simulator.view.gui.GUIApplication;
import javafx.scene.image.Image;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

/**
 * Class containing all the recurring resources used by the application.
 */
public final class Resources {
    /**
     * Application logo.
     */
    public static final Image LOGO = new Image(Objects.requireNonNull(
            GUIApplication.class.getResource("images/logo.png")).toString());
    /**
     * Project name.
     */
    public static final String NAME;
    /**
     * Project version.
     */
    public static final String VERSION;
    /**
     * Project GitHub repository.
     */
    public static final String GITHUB;
    /**
     * Project author.
     */
    public static final String AUTHOR;
    /**
     * Project author GitHub account.
     */
    public static final String ACCOUNT;
    /**
     * Project license.
     */
    public static final String LICENSE;
    /**
     * Project license notice.
     */
    public static final String NOTICE;

    static {
        Properties properties = new Properties();
        try {
            properties.load(Objects.requireNonNull(
                    Resources.class.getResourceAsStream("project.properties")));
            NAME = properties.getProperty("name");
            VERSION = properties.getProperty("version");
            GITHUB = properties.getProperty("github");
            AUTHOR = properties.getProperty("author");
            ACCOUNT = properties.getProperty("account");
            LICENSE = properties.getProperty("license");
            NOTICE = properties.getProperty("notice");
        } catch (IOException e) {
            throw new IllegalStateException("Cannot load resources.", e);
        }
    }

    /**
     * Static class constructor, private to prevent instantiation.
     *
     * @throws IllegalStateException if an attempt to instantiate the class is made
     */
    private Resources() {
        throw new IllegalStateException("Static class cannot be instantiated.");
    }
}

package io.dunham.raku.cli;

import java.util.List;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.cli.EnvironmentCommand;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dunham.raku.RakuConfiguration;
import io.dunham.raku.model.Tag;
import io.dunham.raku.db.TagDAO;


public class AddTagCommand extends EnvironmentCommand<RakuConfiguration> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddTagCommand.class);

    public AddTagCommand(Application<RakuConfiguration> app) {
        super(app, "add-tag", "Adds a new tag to the database");
    }

    @Override
    public void configure(Subparser subparser) {
        super.configure(subparser);

        // Tag name.
        subparser.addArgument("tag")
                 .nargs(1)
                 .help("Name of the tag to add");
    }

    @Override
    public void run(Environment environment, Namespace namespace, RakuConfiguration config) throws Exception {
        // Build JDBI connection and get DAOs
        final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory.build(environment, config.getDataSourceFactory(), "h2");
        final TagDAO tagDao = jdbi.onDemand(TagDAO.class);

        // Parse arguments
        final List<String> nameList = namespace.<String>getList("tag");
        final String tagName = nameList.get(0);
        LOGGER.info("Adding tag: {}", tagName);

        // Create and then save in our DB.
        final Tag t = new Tag(tagName);
        final long id = tagDao.save(t);

        LOGGER.info("Tag created with ID: {}", id);
    }
}

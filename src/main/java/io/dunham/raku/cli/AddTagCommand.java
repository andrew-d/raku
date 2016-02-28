package io.dunham.raku.cli;

import java.util.List;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.cli.EnvironmentCommand;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dunham.raku.RakuConfiguration;
import io.dunham.raku.core.Tag;
import io.dunham.raku.db.TagDAO;
import io.dunham.raku.util.HibernateRunner;


public class AddTagCommand extends EnvironmentCommand<RakuConfiguration> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddTagCommand.class);
    private final HibernateBundle<RakuConfiguration> hibernateBundle;

    public AddTagCommand(Application<RakuConfiguration> app, HibernateBundle<RakuConfiguration> hb) {
        super(app, "add-tag", "Adds a new tag to the database");
        this.hibernateBundle = hb;
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
        final List<String> nameList = namespace.<String>getList("tag");
        final String tagName = nameList.get(0);
        LOGGER.info("Adding tag: {}", tagName);

        final SessionFactory sf = hibernateBundle.getSessionFactory();
        final HibernateRunner hr = new HibernateRunner(sf);
        final TagDAO tagDao = new TagDAO(sf);

        // Create and then save in our DB.
        Tag t = new Tag(tagName);
        hr.withHibernate(() -> {
            tagDao.create(t);
            return null;
        });

        LOGGER.info("Tag created with ID: {}", t.getId());
    }
}

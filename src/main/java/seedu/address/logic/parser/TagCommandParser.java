package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ALLERGY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CONDITION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INSURANCE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG_DELETE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG_EDIT;

import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.TagCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new TagCommand object
 */
public class TagCommandParser implements Parser<TagCommand> {

    @Override
    public TagCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_ALLERGY,
                PREFIX_CONDITION, PREFIX_INSURANCE, PREFIX_TAG_DELETE, PREFIX_TAG_EDIT);

        if (argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE));
        }

        // Parse index from the argument preamble
        Index index = ParserUtil.parseIndex(argMultimap.getPreamble());

        // Check if there are tags to add, delete, or edit
        Set<Tag> allergies = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_ALLERGY));
        Set<Tag> conditions = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_CONDITION));
        Set<Tag> insurances = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_INSURANCE));

        // Check for delete tags (td/)
        Set<Tag> tagsToDelete = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG_DELETE));

        // Check for edit tags (te/)
        Tag oldTag = null, newTag = null;
        boolean isEditTagPresent = argMultimap.getValue(PREFIX_TAG_EDIT).isPresent();

        if (isEditTagPresent) {
            String editTags = argMultimap.getValue(PREFIX_TAG_EDIT).get();
            String[] split = editTags.split("=");

            if (split.length != 2) {
                throw new ParseException("Invalid format for editing a tag. Correct format: te/OLD_TAG=NEW_TAG");
            }
            oldTag = new Tag(split[0].trim());
            newTag = new Tag(split[1].trim());
        }

        // Ensure adding, deleting, and editing cannot happen together
        if (!tagsToDelete.isEmpty() && (!allergies.isEmpty() || !conditions.isEmpty() || !insurances.isEmpty())) {
            throw new ParseException("Cannot add and delete tags in the same command.");
        }
        if (!tagsToDelete.isEmpty() && isEditTagPresent) {
            throw new ParseException("Cannot delete and edit tags in the same command.");
        }
        if (isEditTagPresent && (!allergies.isEmpty() || !conditions.isEmpty() || !insurances.isEmpty())) {
            throw new ParseException("Cannot edit and add tags in the same command.");
        }

        return new TagCommand(index, allergies, conditions, insurances, tagsToDelete, oldTag, newTag);
    }
}

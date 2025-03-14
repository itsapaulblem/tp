package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.ConditionCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ConditionCommand object
 */
public class ConditionCommandParser implements Parser<ConditionCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ConditionCommand
     * and returns a ConditionCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ConditionCommand parse(String args) throws ParseException {
        requireNonNull(args);
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ConditionCommand.MESSAGE_USAGE));
        }

        String[] argComponents = trimmedArgs.split(" ", 2);
        if (argComponents.length < 2) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ConditionCommand.MESSAGE_USAGE));
        }

        Index targetIndex;
        try {
            targetIndex = ParserUtil.parseIndex(argComponents[0]);
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ConditionCommand.MESSAGE_USAGE));
        }

        String condition = argComponents[1].trim();

        // Check if condition is too long (more than 6 words)
        if (condition.split("\\s+").length > 6) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    ConditionCommand.MESSAGE_CONDITION_TOO_LONG));
        }

        return new ConditionCommand(targetIndex, condition);
    }
}

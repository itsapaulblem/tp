package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Adds a medical condition to an existing patient in the address book.
 */
public class ConditionCommand extends Command {

    public static final String COMMAND_WORD = "condition";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds a medical condition to the patient identified " +
            "by the index number used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer) CONDITION (must be 6 words or less)\n"
            + "Example: " + COMMAND_WORD + " 1 diabetes";

    public static final String MESSAGE_SUCCESS = "You have successfully recorded " +
            "%1$s's condition: %2$s";
    public static final String MESSAGE_CONDITION_TOO_LONG = "The input condition is too long! Please summarise it to 6 words or less.";

    private final Index targetIndex;
    private final String condition;

    /**
     * Creates a ConditionCommand to add the specified {@code Condition} to patient at specified index
     */
    public ConditionCommand(Index targetIndex, String condition) {
        requireNonNull(targetIndex);
        requireNonNull(condition);
        this.targetIndex = targetIndex;
        this.condition = condition;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages
                    .MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person patientToUpdate = lastShownList.get(targetIndex.getZeroBased());
        Person updatedPatient = patientToUpdate.addMedicalCondition(condition);

        model.setPerson(patientToUpdate, updatedPatient);
        return new CommandResult(String.format(MESSAGE_SUCCESS, updatedPatient.getName(), condition));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof ConditionCommand)) {
            return false;
        }

        ConditionCommand otherConditionCommand = (ConditionCommand) other;
        return targetIndex.equals(otherConditionCommand.targetIndex)
                && condition.equals(otherConditionCommand.condition);
    }
}

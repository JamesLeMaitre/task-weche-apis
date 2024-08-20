package dev.perogroupe.wecheapis.utils.validations.constraints

import dev.perogroupe.wecheapis.utils.toDate
import dev.perogroupe.wecheapis.utils.validations.validators.BirthDate
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import java.util.Calendar
import java.util.Date
import java.util.Locale

class BirthDateConstraint : ConstraintValidator<BirthDate, String> {

        /**
         * Validates the birthdate to ensure the person is at least 18 years old.
         *
         * @param value the birthdate string to validate
         * @param context the validation context
         * @return true if the person is at least 18 years old, false otherwise
         */
        override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {

                // Check if the input value is null
                if (value == null) {
                        return false // Null values are not valid
                }

                // Convert the input value to a Date object
                val formatDate = value.toDate("dd/MM/yyyy")

                // Get the current date and time
                val calendar = Calendar.getInstance()
                val today = calendar.time

                // Set the calendar to the birthdate
                calendar.time = formatDate

                // Extract the year, month, and day of birth
                val yearOfBirth = calendar.get(Calendar.YEAR)
                val monthOfBirth = calendar.get(Calendar.MONTH)
                val dayOfBirth = calendar.get(Calendar.DAY_OF_MONTH)

                // Reset the calendar to the current date
                calendar.time = today

                // Extract the current year, month, and day
                val currentYear = calendar.get(Calendar.YEAR)
                val currentMonth = calendar.get(Calendar.MONTH)
                val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

                // Calculate the age based on the birth date and current date
                var age = currentYear - yearOfBirth

                // Adjust the age if the current month and day are before the birth month and day
                if (currentMonth < monthOfBirth || (currentMonth == monthOfBirth && currentDay < dayOfBirth)) {
                        age--
                }

                // Check if the calculated age is at least 18
                return age >= 18
        }

}
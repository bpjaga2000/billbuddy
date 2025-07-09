package utils

val numberRegex = Regex("^[0-9]*$")
val emailRegex = "^([a-z-_0-9]*)@[a-z-_0-9]*\\.[a-z]*".toRegex()
val nameRegex = "[a-zA-Z\\s]*".toRegex()
val amountRegex = "[0-9.]*".toRegex()
val allRegex = ".*".toRegex()

fun filter(input: String, regex: Regex): String {
    return if (regex.matches(input))
        input
    else if (regex.matches(input.dropLast(1)))
        input.dropLast(1)
    else ""
}

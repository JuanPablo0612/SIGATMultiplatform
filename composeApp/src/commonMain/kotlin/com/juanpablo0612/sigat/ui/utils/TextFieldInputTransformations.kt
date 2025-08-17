package com.juanpablo0612.sigat.ui.utils

import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer

class DigitOnlyInputTransformation : InputTransformation {
    override fun TextFieldBuffer.transformInput() {
        if (asCharSequence().isNotEmpty() && !asCharSequence().last().isDigit()) {
            revertAllChanges()
        }
    }
}

class NoSpaceAtBeginningInputTransformation : InputTransformation {
    override fun TextFieldBuffer.transformInput() {
        if (asCharSequence().isNotEmpty() && asCharSequence().first().isWhitespace()) {
            revertAllChanges()
        }
    }
}

class LetterAndSpaceOnlyInputTransformation : InputTransformation {
    override fun TextFieldBuffer.transformInput() {
        if (asCharSequence().isNotEmpty() && !asCharSequence().last().isLetter() && !asCharSequence().last().isWhitespace()) {
            revertAllChanges()
        }
    }
}

class NoSpecialCharactersInputTransformation : InputTransformation {
    override fun TextFieldBuffer.transformInput() {
        if (asCharSequence().isNotEmpty() && asCharSequence().any { !it.isLetterOrDigit() && !it.isWhitespace() }) {
            revertAllChanges()
        }
    }
}

class NoSpacesInputTransformation : InputTransformation {
    override fun TextFieldBuffer.transformInput() {
        if (asCharSequence().isNotEmpty() && asCharSequence().any { it.isWhitespace() }) {
            revertAllChanges()
        }
    }
}
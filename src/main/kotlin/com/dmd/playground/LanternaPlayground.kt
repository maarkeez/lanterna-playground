package com.dmd.playground

import com.googlecode.lanterna.SGR.BOLD
import com.googlecode.lanterna.TerminalSize
import com.googlecode.lanterna.TextCharacter
import com.googlecode.lanterna.TextColor
import com.googlecode.lanterna.TextColor.ANSI.*
import com.googlecode.lanterna.gui2.*
import com.googlecode.lanterna.gui2.GridLayout.Alignment.*
import com.googlecode.lanterna.gui2.GridLayout.createHorizontallyFilledLayoutData
import com.googlecode.lanterna.gui2.GridLayout.createLayoutData
import com.googlecode.lanterna.input.KeyType.Escape
import com.googlecode.lanterna.screen.TerminalScreen
import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import com.googlecode.lanterna.terminal.Terminal
import java.lang.Thread.sleep
import kotlin.random.Random

fun main(args: Array<String>) {
    lanternaTutorialFour(args)
}

// Lanterna tutorial 01: https://github.com/mabe02/lanterna/blob/master/docs/tutorial/Tutorial01.md
fun lanternaTutorialOne(args: Array<String>) {

    // Creates a terminal
    val defaultTerminalFactory = DefaultTerminalFactory()
    val terminal = defaultTerminalFactory.createTerminal()
    idle()

    // Print some text in the terminal
    "Hello world\n".forEach { terminal.putCharacter(it) }
    terminal.flush()
    idle()

    // Move cursor
    val startPosition = terminal.cursorPosition
    terminal.cursorPosition = startPosition.withRelativeColumn(3).withRelativeRow(2)
    terminal.flush()
    idle()

    // Print with some color
    terminal.setBackgroundColor(BLUE);
    terminal.setForegroundColor(YELLOW);
    "Yellow on blue\n".forEach { terminal.putCharacter(it) }
    terminal.flush()
    idle()

    // Print in bold
    terminal.cursorPosition = terminal.cursorPosition.withRelativeColumn(3).withRelativeRow(1)
    terminal.enableSGR(BOLD)
    "Yellow on blue BOLD\n".forEach { terminal.putCharacter(it) }
    terminal.flush();
    idle()

    // Reset colors and print again
    terminal.resetColorAndSGR()
    terminal.cursorPosition = terminal.cursorPosition.withColumn(0).withRelativeRow(1);
    "Done\n".forEach { terminal.putCharacter(it) }
    terminal.flush();
    idle()

    beepAndExit(terminal)
}

fun lanternaTutorialTwo(args: Array<String>) {

    // Creates a terminal
    val defaultTerminalFactory = DefaultTerminalFactory()
    val terminal = defaultTerminalFactory.createTerminal()

    // Enter private mode such as 'vi'
    terminal.enterPrivateMode()

    terminal.clearScreen()
    // Hide cursor
    terminal.setCursorVisible(false)

    val textGraphics = terminal.newTextGraphics()
    textGraphics.foregroundColor = WHITE
    textGraphics.backgroundColor = BLACK
    textGraphics.putString(2, 1, "Lanterna Tutorial 2 - Press ESC to exit", BOLD);
    textGraphics.foregroundColor = DEFAULT;
    textGraphics.backgroundColor = DEFAULT;
    textGraphics.putString(5, 3, "Terminal Size: ", BOLD);
    textGraphics.putString(5 + "Terminal Size: ".length, 3, terminal.terminalSize.toString());
    terminal.flush();
    idle()

    // Read some input
    var keyStroke = terminal.readInput()
    while (Escape != keyStroke.keyType) {
        textGraphics.drawLine(5, 4, terminal.terminalSize.columns - 1, 4, ' ');
        textGraphics.putString(5, 4, "Last Keystroke: ", BOLD);
        textGraphics.putString(5 + "Last Keystroke: ".length, 4, keyStroke.toString());
        terminal.flush();
        keyStroke = terminal.readInput();
    }

    terminal.exitPrivateMode()
    idle()

    beepAndExit(terminal)

}

fun lanternaTutorialThree(args: Array<String>) {

    // Creates a terminal
    val defaultTerminalFactory = DefaultTerminalFactory()
    val terminal = defaultTerminalFactory.createTerminal()

    val screen = TerminalScreen(terminal)
    screen.startScreen()
    // Hide cursor
    screen.cursorPosition = null

    val random = Random
    val terminalSize = terminal.terminalSize

    // Save random characteres in the backbuffer of the screen
    for (column in 0..terminalSize.columns) {
        for (row in 0..terminalSize.rows) {
            // Create blank character with random color
            val backgroundColorIndex = random.nextInt(TextColor.ANSI.values().size)
            val backgroundColor = TextColor.ANSI.values()[backgroundColorIndex]
            val textColor = DEFAULT;
            val character = TextCharacter(' ', textColor, backgroundColor)

            // Print character
            screen.setCharacter(column, row, character)
        }
    }

    // Move the content from back-buffer to front-buffer
    screen.refresh()
    idle()
    idle()
    idle()
    idle()
    idle()



    beepAndExit(terminal)

}


fun lanternaTutorialFour(args: Array<String>) {

    // Creates a terminal
    val defaultTerminalFactory = DefaultTerminalFactory()
    val terminal = defaultTerminalFactory.createTerminal()

    val screen = TerminalScreen(terminal)
    screen.startScreen()

    val textGUI: WindowBasedTextGUI = MultiWindowTextGUI(screen)
    val window: Window = BasicWindow("My root window")

    val contentPanel = Panel(GridLayout(2))
    val gridLayout = contentPanel.layoutManager as GridLayout
    gridLayout.horizontalSpacing = 3

    // Title
    val title = Label("This is a label that spans two columns")
    title.layoutData = createLayoutData(BEGINNING, BEGINNING, true, false, 2, 1)
    contentPanel.addComponent(title)

    // Some text boxes
    contentPanel.addComponent(TextBox("Text Box (aligned)"))
    contentPanel.addComponent(TextBox().setLayoutData(createLayoutData(BEGINNING, CENTER)))

    // Password text box
    contentPanel.addComponent(Label("Password box (right aligned)"))
    contentPanel.addComponent(TextBox().setMask('*').setLayoutData(createLayoutData(END, CENTER)))

    // Combo box - read only
    contentPanel.addComponent(Label("Read Only combo box (forzed size)"))
    val readOnlyComboBox = ComboBox<String>(listOf("first-value", "second-value"))
        .setReadOnly(true)
        .setPreferredSize(TerminalSize(20, 1))
    contentPanel.addComponent(readOnlyComboBox)

    // Combo box - editable
    contentPanel.addComponent(Label("Editable combo box (filled)"))
    val editableComboBox = ComboBox<String>(listOf("first-value", "second-value"))
        .setReadOnly(false)
        .setLayoutData(createHorizontallyFilledLayoutData(1))
    contentPanel.addComponent(editableComboBox)

    window.component = contentPanel
    textGUI.addWindowAndWait(window)

    screen.stopScreen()
    beepAndExit(terminal)

}

private fun idle() {
    sleep(1000)
}

private fun beepAndExit(terminal: Terminal) {
    // Beep and exit
    terminal.bell()
    terminal.flush()
    idle()
    terminal.close()
}

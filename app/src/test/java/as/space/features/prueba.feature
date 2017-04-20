Feature: buttons Activity
   I can use the buttons 
    
    Scenario: Start the game
        Given the principal screen in the game
        When i tap in the start button
        Then start the game screen

    Scenario: Move the ship to the left
        Given gameplay screen
        When i tap the right button in the screen
        Then the spaceship moves to the left 10 units

    Scenario: Move the ship to the right
        Given gameplay screen
        When i tap the left button in the screen
        Then the spaceship moves to the right 10 units
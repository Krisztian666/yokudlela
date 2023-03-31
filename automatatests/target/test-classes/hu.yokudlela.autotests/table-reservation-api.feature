@reservation
Feature: Asztalfoglalás

  Scenario Outline: Szabad asztalok lekérdezése, helyes adatokkal
    When get "/reservation/free?begin=<begin>&end=<end>" into "freeTables"
    Then response state <state>
    And exists in the "freeTables" response "[0].name" with string value "<table>"

    @local @release
    Examples:
      |description       | begin                       | end                           |table| state|
      |Korábbi időpontok | 2023-04-21T08%3A43%3A16.479 | 2024-04-21T09%3A43%3A16.479   |A2|200      |


  Scenario Outline: Szabad asztalok lekérdezése, hibás dátumokkal
    When get "/reservation/free?begin=<begin>&end=<end>" into "error"
    Then response state <state>
    And exists in the "error" response "errors.[0]" with string value "<code>"
    @local @release
    Examples:
      | description         | begin                    | end                        |state|code|
      | Korai kezdeti dátum | 2021-03-20T11:18:15.677Z | 2025-03-20T13:18:15.677Z   |400  |error.reservation.begin.past|
      | Korai vég dátum     | 2025-03-20T11:18:15.677Z | 2021-03-20T13:18:15.677Z   |400  |error.reservation.end.past|
      | Korábbi vég dátum   | 2025-03-20T11:18:15.677Z | 2024-03-20T11:18:15.677Z   |400  |error.reservation.end.before.begin|
      | Hibás dátumok       | alma                     | alma                       |400  |error.validation.bind|

  Scenario Outline: Asztalfoglalás
    When post "/reservation/" into "reservation"
        """
          {
            "name": "Hofi Géza",
            "contact": "+36703456789",
            "tableName": "<table>",
            "person": "<person>",
            "begin": "<begin>",
            "end": "<end>"
          }
        """
    Then response state <state>
    And exists in the "reservation" response "errors.[0]" with string value "<code>"

    @local @release
    Examples:
      | description      | table | person | begin                    | end                      | state |code|
      | Nemlétező asztal | A1    |2       | 2025-03-20T13:18:15.677Z | 2025-04-20T13:18:15.677Z | 400   |error.reservation.table.notexist|
      | Korai kezdeti    | A2    |2       | 2022-03-20T11:18:15.677Z | 2025-03-20T13:18:15.677Z   |400  |error.reservation.begin.past|
      | Korai vég dátum  | A2    |2       | 2024-03-20T11:18:15.677Z | 2021-03-20T13:18:15.677Z   |400  |error.reservation.end.past|
      | Korábbi vég dátum| A2    |2       | 2025-03-20T11:18:15.677Z | 2024-03-20T11:18:15.677Z   |400  |error.reservation.end.before.begin|
      | Sikeres          | A2    |2       | 2025-03-20T11:18:15.677Z  | 2024-03-20T11:18:15.677Z | 201   |error.business|


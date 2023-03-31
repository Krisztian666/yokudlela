@table
Feature: Asztalfoglalás

  Scenario Outline: Asztal adatainak lekérdezése név alapján.
    When get "/table/admin/getbyname/<table>" into "res"
    Then response state <state>
    And exists in the "res" response "name" with string value "A1"
    @release
    Examples:
      | table     | state |
      | A1        | 200   |


  Scenario Outline: Asztal letiltása megfelelő jogosultsággal.
    Given form post "/auth/realms/yokudlela/protocol/openid-connect/token" into "auth"
        """
        grant_type=password&scope=openid&username=krisztian.karoczkai@gmail.com&password=krisztian.karoczkai@gmail.com&client_id=account
        """
    And set header param "Authorization" to "Bearer ${response(auth).access_token}"
    And put "/table/admin/enable/<table>"
        """
        """
    When put "/table/admin/disable/<table>"
        """
        """
    Then response state <state>
    And get "/table/admin/getNotAvailableTables" into "res"
    And exists in the "res" response "[0].name" with string value "<table>"
    And put "/table/admin/enable/<table>"
    @release
    Examples:
      | table     | state | userName    |
      | A1        | 200   | First Buyer |


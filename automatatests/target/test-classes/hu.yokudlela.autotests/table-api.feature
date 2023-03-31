@init
Feature: configuration of services


  Scenario Outline: Backend microservice config
    Given set config "<contact>" "https" "<domain>" "<api>" "<key>" "<chanelheader>" "<contextheader>"

    Examples:
      | domain                   | api                             | contact                       | chanelheader | contextheader | key |
      | yokudlela.drhealth.cloud | /table/reservation              | karoczkai_krisztian@4dsoft.hu | channel      | notusecontext |     |
      | yokudlela.drhealth.cloud | /table/admin              | karoczkai_krisztian@4dsoft.hu | channel      | notusecontext |     |
      | yokudlela.drhealth.cloud | /auth                           | karoczkai_krisztian@4dsoft.hu | channel      | notusecontext |     |


  Scenario: get free tables
    When get "freeTables" "/table/reservation/free?start=2021-12-03T10%3A15%3A30&stop=2021-12-03T10%3A15%3A30"
    Then response state 200
    And exists in the "freeTables" response "[0].name" with string value "A1"


  Scenario Outline: Vevő adatainak lekérdezése
    Given post form "auth" "/auth/realms/yokudlela/protocol/openid-connect/token"
        """
        grant_type=password&scope=openid&username=krisztian.karoczkai@gmail.com&password=krisztian.karoczkai@gmail.com&client_id=account
        """
    When get "res" "/table/admin/getbyname/<table>" with Authorization header value "Bearer ${response(auth).access_token}"
    Then response state <state>
    And exists in the "res" response "name" with string value "A1"
    Examples:
      | table     | state | userName    |
      | A1        | 200   | First Buyer |
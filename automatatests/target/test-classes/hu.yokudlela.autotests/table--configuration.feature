@table @reservation
Feature: Asztalfoglalás

  @init
  Scenario Outline: Backend microservice config
    Given set config "<contact>" "<protocoll>" "<domain>" "<api>" "<key>" "<chanelheader>" "<contextheader>"
    @release
    Examples:
      |protocoll| domain                         | api                             | contact                       | chanelheader | contextheader | key |
      |https    | yokudlela.drhealth.cloud/table | /reservation              | karoczkai_krisztian@4dsoft.hu | channel      | notusecontext |     |
      |https    | yokudlela.drhealth.cloud/table | /admin              | karoczkai_krisztian@4dsoft.hu | channel      | notusecontext |     |
      |https    | yokudlela.drhealth.cloud/table | /auth                           | karoczkai_krisztian@4dsoft.hu | channel      | notusecontext |     |

    @local
    Examples:
      |protocoll| domain         | api                 | contact                       | chanelheader | contextheader | key |
      |http     | localhost:8080 | /reservation        | karoczkai_krisztian@4dsoft.hu | channel      | notusecontext |     |
      |http     | localhost:8080 | /admin              | karoczkai_krisztian@4dsoft.hu | channel      | notusecontext |     |
      |http     | localhost:8080 | /auth               | karoczkai_krisztian@4dsoft.hu | channel      | notusecontext |     |

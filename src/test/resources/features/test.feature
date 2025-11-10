Feature: Gestion des tâches

  Scenario: Ajouter une tâche
    Given je suis sur la page TodoMVC
    When j’ajoute la tâche "Acheter du café"
    Then la tâche "Acheter du café" est visible dans la liste

  Scenario: Supprimer une tâche
    Given je suis sur la page TodoMVC
    When j’ajoute la tâche "Faire le ménage"
    And je supprime la tâche "Faire le ménage"
    Then la tâche "Faire le ménage" n’est plus visible dans la liste

  Scenario: Marquer une tâche comme terminée
    Given je suis sur la page TodoMVC
    When j’ajoute la tâche "Lire un livre"
    And je coche la tâche "Lire un livre"
    Then la tâche "Lire un livre" apparaît comme terminée
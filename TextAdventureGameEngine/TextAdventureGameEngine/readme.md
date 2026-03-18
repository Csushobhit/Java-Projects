+-----------+         +-----------+         +-----------+
|  Player   |         |   Room    |         |   Item    |
+-----------+         +-----------+         +-----------+
| - currentRoomName: String |----->| - name: String  |<>----->| - name: String  |
| - inventory: List<Item>   | 1   * | - description: String |         | - description: String |
+-----------+         | - exits: Map<String, String>|         +-----------+
      |               | - items: List<Item>     |
      |               +-----------+         ^
      |                     |               |
      | 1..*                | *             | *
      `---------------------'---------------'
             (carries)           (contains)
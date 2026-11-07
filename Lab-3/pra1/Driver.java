public class Driver {
    public static void main(String[] args) {
        countDistinctPoints();
        findFirstDuplicateCard();
    }

    private static void countDistinctPoints() {
        Point[] points = {
            new Point(1, 2),
            new Point(3, 4),
            new Point(1, 2),
            new Point(5, 6),
            new Point(3, 4)
        };

        int distinctCount = 0;
        for (int i = 0; i < points.length; i++) {
            boolean alreadySeen = false;
            for (int j = 0; j < i; j++) {
                if (points[i].equals(points[j])) {
                    alreadySeen = true;
                    break;
                }
            }
            if (!alreadySeen) {
                distinctCount++;
            }
        }

        System.out.println("Distinct: " + distinctCount);
    }

    private static void findFirstDuplicateCard() {
        Card[] cardsToAdd = {
            new Card("Ace", "Spades"),
            new Card("King", "Hearts"),
            new Card("Ace", "Spades")
        };
        Card[] addedCards = new Card[cardsToAdd.length];
        int addedCount = 0;

        for (Card card : cardsToAdd) {
            boolean duplicateFound = false;
            for (int i = 0; i < addedCount; i++) {
                if (card.equals(addedCards[i])) {
                    System.out.println("Duplicate found: " + card);
                    duplicateFound = true;
                    break;
                }
            }
            if (!duplicateFound) {
                addedCards[addedCount] = card;
                addedCount++;
            }
        }
    }
}

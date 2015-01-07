package simplegrailsquartzshceduler

class Frequency implements Serializable {

    private static final long serialVersionUID = 1L

    static constraints = {
        cron nullable: true
    }

    String tag
    String description
    Integer amount
    String cron
    Integer ordering
}

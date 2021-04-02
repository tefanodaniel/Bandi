package dao;
import exceptions.DaoException;
import model.SongOfTheWeekEvent;
import model.SongOfTheWeekSubmission;
import java.util.List;
import java.util.Set;

public interface SotwEventDao {
    /** Create a new Song of the Week Event with the following necessary parameters.
     *
     * @param eventId A unique event Id.
     * @param adminId The admin who creates the event.
     * @param start_week The start day of the event week.
     * @param end_week The end day of the event week.
     * @param songId The unique Id of song associated with the event.
     * @return created SongOfTheWeekEvent object
     * @throws DaoException
     */
    SongOfTheWeekEvent create(String eventId, String adminId, String start_week, String end_week, String songId) throws DaoException;

    /** Create a new Song of the Week Event with the following necessary parameters.
     *
     * @param eventId A unique event Id.
     * @param adminId The admin who creates the event.
     * @param start_week The start day of the event week.
     * @param end_week The end day of the event week.
     * @param songId The unique Id of song associated with the event.
     * @param submissions
     * @return created SongOfTheWeekEvent object
     * @throws DaoException
     */
    SongOfTheWeekEvent create(String eventId, String adminId, String start_week, String end_week, String songId, Set<String> submissions) throws DaoException;


    /** Read a Song of the Week event given its unique event id.
     *
     * @param eventId
     * @return retrieved SongOfTheWeekEvent object
     * @throws DaoException
     */
    SongOfTheWeekEvent read(String eventId) throws DaoException;

    /** Read all Song of the Week Events.
     *
     * @return List of all SongOfTheWeekEvent objects from databse
     * @throws DaoException
     */
    List<SongOfTheWeekEvent> readAll() throws DaoException;

    /** Update the admin associated with a specific event.
     *
     * @param eventId
     * @param adminId
     * @return
     * @throws DaoException
     */
    SongOfTheWeekEvent updateAdmin(String eventId, String adminId) throws DaoException;


    /** Update the Start Week of specific Event.
     *
     * @param eventId
     * @param new_week
     * @return Updated SongOfTheWeekEvent object
     * @throws DaoException
     */
    SongOfTheWeekEvent updateStartWeek(String eventId, String new_week) throws DaoException;

    /** Update the End Week of a specific Event.
     *
     * @param eventId
     * @param new_week
     * @return Updated SongOfTheWeekEvent object
     * @throws DaoException
     */
    SongOfTheWeekEvent updateEndWeek(String eventId, String new_week) throws DaoException;

    /** Update the Song associated with a specific event via unique id.
     *
     * @param eventId
     * @param songId
     * @return Updated SongOfTheWeekEvent object
     * @throws DaoException
     */
    SongOfTheWeekEvent updateSong(String eventId, String songId) throws DaoException;

    /** Read all Submissions for a specific event
     *
     * @param eventId
     * @return List of all corresponding audio/video submissions
     * @throws DaoException
     */
    Set<String> readAllSubmissionsGivenEvent(String eventId) throws DaoException;

    /** Add a submission to the specific event
     *
     * @param eventId
     * @param submissionId
     * @return the added submission object
     * @throws DaoException
     */
    SongOfTheWeekEvent addSubmissionToEvent(String eventId, String submissionId) throws DaoException;

    /** Remove a submission from a specific event
     *
     * @param eventId
     * @param submissionId
     * @return the removed submission object
     * @throws DaoException
     */
    SongOfTheWeekEvent removeSubmissionFromEvent(String eventId, String submissionId) throws DaoException;


    /** Delete a specific song of the week event from database.
     *
     * @param eventId
     * @return
     * @throws DaoException
     */
    SongOfTheWeekEvent deleteEvent(String eventId) throws DaoException; // this should delete each submission asssociated with it in return

}

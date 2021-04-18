package dao;
import model.SongOfTheWeekSubmission;
import exceptions.DaoException;
import java.util.List;
import java.util.Set;

public interface SotwSubmissionDao {
    /** Create a A/V submission from a specific musician
     *
     * @param submissionId
     * @param musicianId
     * @param avSubmission
     * @param instruments
     * @return
     * @throws DaoException
     */
    SongOfTheWeekSubmission create(String submissionId, String musicianId, String musician_name,
                                   String avSubmission, Set<String> instruments) throws DaoException;

    /** Read a submission given unique id
     *
     * @param submissionId
     * @return
     * @throws DaoException
     */
    SongOfTheWeekSubmission read(String submissionId) throws DaoException;

    /** Read all submissions
     *
     * @return
     * @throws DaoException
     */
    List<SongOfTheWeekSubmission> readAll() throws DaoException;


    /** Update the Audio/Video link for a submission
     *
     * @param submissionId
     * @param avSubmission
     * @return
     * @throws DaoException
     */
    SongOfTheWeekSubmission updateAVSubmission(String submissionId, String avSubmission) throws DaoException;

    /** Update the Instruments associated with a submission
     *
     * @param submissionId
     * @param instruments
     * @return
     * @throws DaoException
     */
    SongOfTheWeekSubmission updateInstruments(String submissionId, Set<String> instruments) throws DaoException;

    /** Delete a submission given unique id.
     *
     * @param submissionId
     * @return
     * @throws DaoException
     */
    SongOfTheWeekSubmission delete(String submissionId) throws DaoException;
}

package evaluation.metrics;

import core.AbstractPlayer;
import core.Game;
import evaluation.listeners.MetricsGameListener;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Records all data per player combination.
 */
public abstract class AbstractTournamentMetric extends  AbstractMetric
{
    // Data logger, wrapper around a library that logs data into a table
    private final Map<List<AbstractPlayer>,IDataLogger> dataLoggers = new HashMap<>();

    public AbstractTournamentMetric() {
        super();
    }
    public AbstractTournamentMetric(Event.GameEvent... args) {
        super(args);
    }

    /**
     * @param listener - game listener object, with access to the game itself and loggers
     * @param e        - event, including game event type, state, action and player ID (if these properties are relevant, they may not be set depending on event type)
     * @param records  - map of data points to be filled in by the metric with recorded information
     * @return - true if the data saved in records should be recorded indeed, false otherwise. The metric
     * might want to listen to events for internal saving of information, but not actually record it in the data table.
     */
    protected abstract boolean _run(MetricsGameListener listener, Event e, Map<String, Object> records);

    /**
     * @return set of game events this metric should record information for.
     */
    public abstract Set<Event.GameEvent> getDefaultEventTypes();

    public void reset() {
        super.reset();
        for (IDataLogger logger : dataLoggers.values()) {
            logger.reset();
        }
    }

    /**
     * Initialize columns separately when we have access to the game.
     * @param game - game to initialize columns for
     */
    public void init(Game game, int nPlayers, List<String> playerNames) {
        // Do nothing here, we init specially
    }

    public void tournamentInit(Game game, int nPlayers, List<String> playerNames, List<AbstractPlayer> matchup) {
        // Create a data logger for this matchup
        if (dataLoggers.containsKey(matchup)) {
            setDataLogger(dataLoggers.get(matchup));
        } else {
            IDataLogger logger = dataLogger.create();
            dataLoggers.put(matchup, logger);
            logger.init(game, nPlayers, playerNames);
            setDataLogger(logger);
        }
    }

    /**
     * Produces reports of data for this metric.
     * @param folderName - name of the folder to save the reports in
     * @param reportTypes - list of report types to produce
     * @param reportDestinations - list of report destinations to produce
     */
    public void processFinishedGames(String folderName, List<IDataLogger.ReportType> reportTypes, List<IDataLogger.ReportDestination> reportDestinations)
    {
        //DataProcessor with compatibility assertion:
        IDataProcessor dataProcessor = getDataProcessor();
        assert dataProcessor.getClass().isAssignableFrom(dataLogger.getDefaultProcessor().getClass()) :
                "Using a Data Processor " + dataProcessor.getClass().getSimpleName() + " that is not compatible with the Data Logger "
                        + dataLogger.getClass().getSimpleName() + ". Data Processor and Data Logger must be using the same library, and " +
                        " the Data Processor must extend the Data Logger's default processor.";

        for (Map.Entry<List<AbstractPlayer>, IDataLogger> e: dataLoggers.entrySet()) {
            String folder = folderName + "/" + e.getKey().toString();
            // Make folder if it doesn't exist
            File f = new File(folder);
            boolean success = true;
            if (!f.exists()) {
                success = f.mkdir();
            }
            if (!success) {
                throw new AssertionError("Could not create folder " + folder + " for data logger " + e.getValue().getClass().getSimpleName() + " for metric " + this.getClass().getSimpleName() + "!");
            }
            IDataLogger logger = e.getValue();

            for (int i = 0; i < reportTypes.size(); i++) {
                IDataLogger.ReportType reportType = reportTypes.get(i);
                IDataLogger.ReportDestination reportDestination;
                if (reportDestinations.size() == 1) reportDestination = reportDestinations.get(0);
                else reportDestination = reportDestinations.get(i);

                if (reportType == IDataLogger.ReportType.RawData) {
                    if (reportDestination == IDataLogger.ReportDestination.ToFile || reportDestination == IDataLogger.ReportDestination.ToBoth) {
                        dataProcessor.processRawDataToFile(logger, folder);
                    }
                    if (reportDestination == IDataLogger.ReportDestination.ToConsole || reportDestination == IDataLogger.ReportDestination.ToBoth) {
                        dataProcessor.processRawDataToConsole(logger);
                    }
                } else if (reportType == IDataLogger.ReportType.Summary) {
                    if (reportDestination == IDataLogger.ReportDestination.ToFile || reportDestination == IDataLogger.ReportDestination.ToBoth) {
                        dataProcessor.processSummaryToFile(logger, folder);
                    }
                    if (reportDestination == IDataLogger.ReportDestination.ToConsole || reportDestination == IDataLogger.ReportDestination.ToBoth) {
                        dataProcessor.processSummaryToConsole(logger);
                    }
                } else if (reportType == IDataLogger.ReportType.Plot) {
                    if (reportDestination == IDataLogger.ReportDestination.ToFile || reportDestination == IDataLogger.ReportDestination.ToBoth) {
                        dataProcessor.processPlotToFile(logger, folder);
                    }
                    if (reportDestination == IDataLogger.ReportDestination.ToConsole || reportDestination == IDataLogger.ReportDestination.ToBoth) {
                        dataProcessor.processPlotToConsole(logger);
                    }
                }
            }
        }
    }
}

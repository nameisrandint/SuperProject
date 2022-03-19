package algo.conf;

import algo.segewick.Board;
import algo.segewick.PuzzleChecker;
import algo.segewick.Solver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import algo.segewick.PuzzleReader;

@Configuration
@PropertySource("classpath:application.properties")
public class SpringConfiguration {

    @Bean
    public Solver getSolver(@Autowired Board board) {
        return new Solver(board);
    }

    @Bean
    public Board getBoard(@Autowired PuzzleReader puzzleReader) {
        return new Board(puzzleReader.makeTestPuzzle());
    }

    @Bean
    public PuzzleReader getPuzzleReader(@Autowired ConfigurationService configurationService) {
        return new PuzzleReader(configurationService.getPuzzlesFileName());
    }

    @Bean
    public ConfigurationService getConfigurationService(
            @Value("${super-project.puzzles.filename}") String fileName
    ) {
        return new ConfigurationService(fileName);
    }

    @Bean
    public PuzzleChecker getPuzzleChecker(
            @Autowired ConfigurationService configurationService,
            @Autowired Solver solver
    ) {
        return new PuzzleChecker(solver, configurationService.getPuzzlesFileName());
    }
}

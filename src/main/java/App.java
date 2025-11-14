import domine.Lexer;
import domine.Parser;
import domine.Token;
import resources.message.Message;

import java.util.List;
import java.util.Scanner;

public class App 
{

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Message.showInitialMessage();

        while (true) {
            Message.askInput();
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("salir") || input.equalsIgnoreCase("exit")) {
                break;
            }

            try {
                // Fase 1: Análisis léxico (tokenización)
                Lexer lexer = new Lexer(input);
                List<Token> tokens = lexer.tokenize();

                Message.showTokens(tokens);

                Parser parser = new Parser(tokens);
                double result = parser.parse();

                Message.showAstTree(parser);

                Message.showResult(result);

                parser.resetParser();

            } catch (Exception e) {
                Message.showErrorMessage(e);
            }

            System.out.println();
        }
        scanner.close();
        Message.showGoodByeMessage();
    }
}

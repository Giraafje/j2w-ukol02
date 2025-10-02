package cz.czechitas.java2webapps.ukol2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Controller
public class MainController {
  private final Random random = new Random();
  private final List<String> quotes = readAllLines("citaty.txt");

  // Adjusting the main page, therefore the route is "/"
  @GetMapping("/")
  public ModelAndView dynamic() throws IOException {
    ModelAndView modelAndView = new ModelAndView("dynamic");
    int randomNumber = getRandomNumber(quotes.size());
    modelAndView.addObject("quote", quotes.get(randomNumber));
    modelAndView.addObject("imageNumber", getRandomNumber(10) + 1);
    return modelAndView;
  }

  private static List<String> readAllLines(String resource) {
    //Soubory z resources se získávají pomocí classloaderu. Nejprve musíme získat aktuální classloader.
    ClassLoader classLoader=Thread.currentThread().getContextClassLoader();

    //Pomocí metody getResourceAsStream() získáme z classloaderu InpuStream, který čte z příslušného souboru.
    //Následně InputStream převedeme na BufferedRead, který čte text v kódování UTF-8
    try(
        InputStream inputStream = classLoader.getResourceAsStream(resource);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(inputStreamReader)
    ){
      return reader
          .lines()                        // Metoda lines() vrací stream řádků ze souboru.
          .collect(Collectors.toList());  // Pomocí kolektoru převedeme Stream<String> na List<String>.
    } catch (IOException e) {
      throw new RuntimeException("Nepodařilo se načíst soubor " + resource, e);
    }
  }

  private int getRandomNumber(int range) {
    return random.nextInt(range);
  }
}

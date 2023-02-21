package com.example.marketour.controllers;

import com.example.marketour.model.entities.*;

import com.example.marketour.services.TourPagesService;
import com.example.marketour.services.TourReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping
public class RouteController {
    private final TourController tourController;
    private final ImageController imageController;
    private final UserController userController;
    private final TourPageController tourPageController;
    private final TransactionController transactionController;
    private final TourPagesService tourPagesService;

    private final TourReviewService tourReviewService;

    public RouteController(TourController tourController, ImageController imageController, UserController userController, TourPageController tourPageController, TransactionController transactionController, TourPagesService tourPagesService, TourReviewService tourReviewService) {
        this.tourController = tourController;
        this.imageController = imageController;
        this.userController = userController;
        this.tourPageController = tourPageController;
        this.transactionController = transactionController;
        this.tourPagesService = tourPagesService;
        this.tourReviewService = tourReviewService;
    }

    @GetMapping("/")
    public String indexPage(HttpServletRequest request) {
        var session = request.getSession(true);
        if ((session.getAttribute("errorMessage") != null)) {
            session.removeAttribute("errorMessage");
        }
        if ((session.getAttribute("registerErrorMessage") != null)) {
            session.removeAttribute("registerErrorMessage");
        }
        return "home";
    }

    @GetMapping(value = "/login")
    String login(@ModelAttribute("user") User user) {
        return "login";
    }


    @GetMapping(value = "/pageEdit")
    String pageEdit(Model model, HttpServletRequest httpServletRequest, @RequestParam Map<String, String> params) {
        if (httpServletRequest.getSession().getAttribute("user") == null) {
            return "redirect:/";
        }
        final var pageId = params.get("pageId");
        final var existingPage = tourPageController.getById(pageId, httpServletRequest).getBody();
        if (existingPage instanceof TourPage) {
            model.addAttribute("page", ((TourPage) existingPage).getPage());
            model.addAttribute("audioFile", Base64.getEncoder().encodeToString(((TourPage) existingPage).getAudio().getData()));
            model.addAttribute("imageFile", Base64.getEncoder().encodeToString(((TourPage) existingPage).getImage().getData()));
            model.addAttribute("tourName", ((TourPage) existingPage).getTour().getName());
            model.addAttribute("tourPage", existingPage);
        }
        return "pageEdit";
    }

    @GetMapping(value = "/pageCreate")
    String pageCreate(Model model, HttpServletRequest httpServletRequest, @RequestParam Map<String, String> params) {
        if (httpServletRequest.getSession().getAttribute("user") == null) {
            return "redirect:/";
        }
        var tour = new Tour();
        //Adding page to the existing tour
        if (params.containsKey("tourId")) {
            tour.setTourId(Long.valueOf(params.get("tourId")));
            final var pages = tourPageController.getAllTourPages(Long.valueOf(params.get("tourId"))).getBody();
            if (pages != null && !pages.isEmpty()) {
                model.addAttribute("page", pages.size());
            } else {
                model.addAttribute("page", 0);
            }
            final var tourResult = tourController.getTour(httpServletRequest, params.get("tourId"));
            if (tourResult.getBody() != null) {
                tour = (Tour) tourResult.getBody();
            }
        }
        //Adding page for the new tour
        else {
            tour.setDescription(params.get("description"));
            tour.setPrice(Double.valueOf(params.get("price")));
            tour.setName(params.get("name"));
            tour.setVisibleOnMarket(false);
            model.addAttribute("page", 0);
        }
        final var session = httpServletRequest.getSession(true);
        session.setAttribute("tour", tour);
        model.addAttribute("tour", tour);

        return "pageCreate";
    }

    @GetMapping(value = "/editTour/{tourId}")
    String editTour(Model model, HttpServletRequest request, @PathVariable String tourId) {
        if (request.getSession().getAttribute("user") == null) {
            return "redirect:/";
        }
        final var user = (User) request.getSession().getAttribute("user");
        model.addAttribute("user", user);
        final var tour = (Tour) tourController.getTour(request, tourId).getBody();
        final var tourPagesResult = tourPageController.getAllTourPages(Long.valueOf(tourId));
        model.addAttribute("existingTour", tour);
        final var imagesBase64 = Objects.requireNonNull(tourPagesResult.getBody()).stream().map(tourPage -> Map.entry(tourPage.getTourPageId(), Base64.getEncoder().encodeToString(tourPage.getImage().getData()))).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        final var audioBase64 = Objects.requireNonNull(tourPagesResult.getBody()).stream().map(tourPage -> Map.entry(tourPage.getTourPageId(), Base64.getEncoder().encodeToString(tourPage.getAudio().getData()))).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        model.addAttribute("imagesBase64", imagesBase64);
        model.addAttribute("audioBase64", audioBase64);
        model.addAttribute("tourPages", tourPagesResult.getBody().stream().sorted(Comparator.comparing(TourPage::getPage)).collect(Collectors.toList()));
        final var lng = Objects.requireNonNull(Objects.requireNonNull(tour).getTourPages().stream().filter(tourPage -> tourPage.getPage() == 0).findFirst().orElse(null)).getLocation().getLongitude();
        final var lat = Objects.requireNonNull(Objects.requireNonNull(tour).getTourPages().stream().filter(tourPage -> tourPage.getPage() == 0).findFirst().orElse(null)).getLocation().getLatitude();

        model.addAttribute("startLongitude", lng);
        model.addAttribute("startLatitude", lat);

        return "editTour";
    }

    @GetMapping(value = "/startTour/{tourId}")
    String startTour(Model model, HttpServletRequest request, @PathVariable String tourId) {
        if (request.getSession().getAttribute("user") == null) {
            return "redirect:/";
        }
        final var user = (User) request.getSession().getAttribute("user");
        model.addAttribute("user", user);
        final var tourPagesResult = tourPageController.getAllTourPages(Long.valueOf(tourId));
        final var tour = (Tour) tourController.getTour(request, tourId).getBody();
        model.addAttribute("existingTour", tour);
        model.addAttribute("tourPages", tourPagesResult.getBody());
        final var audioBase64 = Objects.requireNonNull(tourPagesResult.getBody()).stream().map(tourPage -> Map.entry(tourPage.getTourPageId(), Base64.getEncoder().encodeToString(tourPage.getAudio().getData()))).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        final var imagesBase64 = Objects.requireNonNull(tourPagesResult.getBody()).stream().map(tourPage -> Map.entry(tourPage.getTourPageId(), Base64.getEncoder().encodeToString(tourPage.getImage().getData()))).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        model.addAttribute("imagesBase64", imagesBase64);
        model.addAttribute("audioBase64", audioBase64);
        model.addAttribute("tourSize", tourPagesResult.getBody().size());
        return "startTour";
    }

    @GetMapping(value = "/newTour")
    String newTour(Model model, HttpServletRequest httpServletRequest) {
        if (httpServletRequest.getSession().getAttribute("user") == null) {
            return "redirect:/";
        }
        final var user = (User) httpServletRequest.getSession().getAttribute("user");
        model.addAttribute("user", user);
        return "newTour";
    }

    @GetMapping(value = "/register")
    String register(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("cities", City.values());
        model.addAttribute("countries", Country.values());
        model.addAttribute("userTypes", UserType.values());
        return "register";
    }

    @GetMapping(value = "/main")
    String main(HttpServletRequest httpServletRequest, Model model) throws IOException {
        final var userSpecificTours = tourController.getAllToursOfThisUser(httpServletRequest, model);

        final var allTours = tourController.getAllTours(httpServletRequest);

        final var imageMap = ((ArrayList<Tour>) allTours.getBody()).stream().map(tour -> Map.entry(tour.getTourId(), Objects.requireNonNull(Base64.getEncoder().encodeToString(Objects.requireNonNull((Image) imageController.getFirstPageImage(tour.getTourId(), httpServletRequest).getBody()).getData())))).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        final var user = (User) httpServletRequest.getSession().getAttribute("user");
        model.addAttribute("filter", new Filter(null, null, null, true));

        final var allToursOnMarketSorted = tourController.getAllToursOnMarket(httpServletRequest, model);
        model.addAttribute("userTours", userSpecificTours.getBody());
        model.addAttribute("allToursSorted", ((ArrayList<Tour>) allToursOnMarketSorted.getBody()).stream().filter(tour -> tour != null && !((ArrayList<Tour>) userSpecificTours.getBody()).stream().map(tour1 -> tour1.getTourId()).collect(Collectors.toList()).contains(tour.getTourId())).collect(Collectors.toList()));
        model.addAttribute("filter", null);
        final var allToursOnMarket = tourController.getAllToursOnMarket(httpServletRequest, model);
        model.addAttribute("allTours", ((ArrayList<Tour>) allToursOnMarket.getBody()).stream().filter(tour -> !((ArrayList<Tour>) userSpecificTours.getBody()).stream().map(tour1 -> tour1.getTourId()).collect(Collectors.toList()).contains(tour.getTourId())).collect(Collectors.toList()));
        model.addAttribute("imageMap", imageMap);

        if (user.getImage() != null) {
            model.addAttribute("userAvatar", Base64.getEncoder().encodeToString(user.getImage().getData()));
        } else {
            File newFile = new File("src/main/resources/static/img/covo.png");
            BufferedImage originalImage = ImageIO.read(newFile);
            ByteArrayOutputStream oStream = new ByteArrayOutputStream();
            ImageIO.write(originalImage, "png", oStream);
            byte[] imageInByte = oStream.toByteArray();
            model.addAttribute("userAvatar", Base64.getEncoder().encodeToString(imageInByte));

        }
        final var transactionsResult = transactionController.getAllTransactionsOfUser(httpServletRequest);
        if (transactionsResult.getStatusCode() == HttpStatus.OK) {
            model.addAttribute("transactions", transactionsResult.getBody());
        }
        model.addAttribute("isTourist", user.getUserType() == UserType.tourist);
        model.addAttribute("user", user);
        return "main";
    }

    @GetMapping(value = "/logout")
    String logout(HttpServletRequest request) {
        final var response = userController.logout(request);
        return "redirect:/";
    }

}
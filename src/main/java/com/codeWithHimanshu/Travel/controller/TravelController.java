package com.codeWithHimanshu.Travel.controller;

import com.codeWithHimanshu.Travel.entity.Travel;
import com.codeWithHimanshu.Travel.repository.TravelRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/travels")
public class TravelController {

    @Autowired
    private TravelRepository travelRepo;

    private final Path uploadDir = Paths.get("uploads");

    public TravelController() throws IOException {
        if (!Files.exists(uploadDir)) Files.createDirectories(uploadDir);
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("travels", travelRepo.findAll());
        return "travels/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("travel", new Travel());
        return "travels/form";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute Travel travel, BindingResult br,
                         @RequestParam("imageFile") MultipartFile imageFile, Model model) {
        if (br.hasErrors()) {
            return "travels/form";
        }
        // handle image
        if (imageFile != null && !imageFile.isEmpty()) {
            String ext = Optional.ofNullable(imageFile.getOriginalFilename())
                    .filter(n -> n.contains("."))
                    .map(n -> n.substring(n.lastIndexOf('.'))).orElse("");
            String filename = UUID.randomUUID().toString() + ext;
            try {
                Files.copy(imageFile.getInputStream(), uploadDir.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
                travel.setImage(filename);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        travelRepo.save(travel);
        return "redirect:/travels";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Travel t = travelRepo.findById(id).orElseThrow();
        model.addAttribute("travel", t);
        return "travels/form";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id, @Valid @ModelAttribute Travel travel, BindingResult br,
                       @RequestParam("imageFile") MultipartFile imageFile) {
        if (br.hasErrors()) return "travels/form";
        Travel existing = travelRepo.findById(id).orElseThrow();
        existing.setTitle(travel.getTitle());
        existing.setLocation(travel.getLocation());
        existing.setDescription(travel.getDescription());
        existing.setPrice(travel.getPrice());
        if (imageFile != null && !imageFile.isEmpty()) {
            String ext = Optional.ofNullable(imageFile.getOriginalFilename())
                    .filter(n -> n.contains("."))
                    .map(n -> n.substring(n.lastIndexOf('.'))).orElse("");
            String filename = UUID.randomUUID().toString() + ext;
            try {
                Files.copy(imageFile.getInputStream(), uploadDir.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
                existing.setImage(filename);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        travelRepo.save(existing);
        return "redirect:/travels";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        travelRepo.deleteById(id);
        return "redirect:/travels";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model) {
        Travel t = travelRepo.findById(id).orElseThrow();
        model.addAttribute("travel", t);
        return "travels/view";
    }
}

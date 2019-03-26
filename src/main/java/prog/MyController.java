package prog;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
public class MyController {

    private Map<Long, byte[]> photos = new ConcurrentHashMap<>();

    @RequestMapping("/")
    public String onIndex() {
        return "index";
    }

    @RequestMapping(value = "/add_photo", method = RequestMethod.POST)
    public String onAddPhoto(Model model, @RequestParam MultipartFile photo) {
        if (photo.isEmpty()) {
            throw new PhotoErrorException("Photo is empty");
        }

        try {
            long id = System.currentTimeMillis();
            photos.put(id, photo.getBytes());
            model.addAttribute("photo_id", id);
            return "result";
        } catch (IOException e) {
            throw new PhotoErrorException("Error");
        }
    }

    @RequestMapping("/photo/{photo_id}")
    public ResponseEntity<byte[]> onPhoto(@PathVariable("photo_id") long id) {
        return photoById(id);
    }

    @RequestMapping(value = "/view", method = RequestMethod.POST)
    public ResponseEntity<byte[]> onView(@RequestParam("photo_id") long id) {
        return photoById(id);
    }

    @RequestMapping("/delete/{photo_id}")
    public String onDelete(@PathVariable("photo_id") long id) {
        if (photos.remove(id) == null)
            throw new PhotoNotFoundException();
        else
            return "index";
    }

    @ExceptionHandler(Exception.class)
    public String error() {
        return "error";
    }

    private ResponseEntity<byte[]> photoById(long id) {
        byte[] bytes = photos.get(id);
        if (bytes == null)
            throw new PhotoNotFoundException();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/show", params = "show", method = RequestMethod.POST)
    public String showPhoto(Model model) {
        if (photos.size() == 0) model.addAttribute("no_rez", "No photos");
        else model.addAttribute("photos", photos.keySet());
        return "index";
    }

    @RequestMapping(value = "/show", params = "del", method = RequestMethod.POST)
    public String delPhoto(@RequestParam(value = "id_del", required = false) String[] delId) {
        if (delId != null) {
            for (String item : delId) {
                Long id = Long.valueOf(item);
                photos.remove(id);
            }
        }
        return "index";
    }

    @RequestMapping(value = "/show", params = "zip", method = RequestMethod.POST)
    public ResponseEntity<byte[]> photoGet(@RequestParam(value = "id_del", required = false) String[] zipId) {
        if (zipId == null)
            throw new PhotoNotFoundException();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (ZipOutputStream zos = new ZipOutputStream(baos)) {
                for (String item : zipId) {
                    Long id = Long.valueOf(item);
                    ZipEntry ze = new ZipEntry(id + ".jpg");
                    zos.putNextEntry(ze);
                    zos.write(photos.get(id));
                    zos.closeEntry();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] bytes = baos.toByteArray();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/zip"));
            headers.setContentDispositionFormData("attachment", "photos.zip");
            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }
}
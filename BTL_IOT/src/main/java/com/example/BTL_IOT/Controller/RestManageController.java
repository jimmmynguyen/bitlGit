package com.example.BTL_IOT.Controller;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.BTL_IOT.Model.Account;
import com.example.BTL_IOT.Model.Card;
import com.example.BTL_IOT.Model.Device;
import com.example.BTL_IOT.Model.Employee;
import com.example.BTL_IOT.Model.History;
import com.example.BTL_IOT.Model.HistoryDto;
import com.example.BTL_IOT.Model.HistoryDto2;
import com.example.BTL_IOT.Repository.HistoryRepository;
import com.example.BTL_IOT.Service.AccountService;
import com.example.BTL_IOT.Service.CardService;
import com.example.BTL_IOT.Service.DeviceService;
import com.example.BTL_IOT.Service.HistoryService;

import jakarta.annotation.PreDestroy;
import java.text.SimpleDateFormat;
import java.text.SimpleDateFormat;

@RestController
@CrossOrigin
@RequestMapping("/manage/data")
public class RestManageController {
	@Autowired
	private DeviceService deviceService;
	
	@Autowired
	private CardService cardService;
	
	@Autowired
	private HistoryService historyService;
	
	@Autowired
	private AccountService accountService;
	
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
	
	public LocalDateTime convertToHCMTimeZone(String dateTimeStr) {
        Instant instant = Instant.parse(dateTimeStr);
        ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh"); // Múi giờ +7 TPHCM
        return LocalDateTime.ofInstant(instant, zoneId);
    }
		
	@Scheduled(fixedDelay = 2000)
    @GetMapping
    public ArrayList<HistoryDto2> getAllDataNow() {
        Future<ArrayList<HistoryDto2>> future = executor.submit(() -> {
            ArrayList<HistoryDto2> list = new ArrayList<>();
            String apiUrl = "https://api.thingspeak.com/channels/2324544/fields/1.json?api_key=TJWSBICTTH93Z53G";
            try {
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    reader.close();

                    // Phân tích JSON từ nội dung trả về
                    JSONObject jsonObject = new JSONObject(response.toString());

                    // Lấy mảng "feeds" từ JSON
                    JSONArray feedsArray = jsonObject.getJSONArray("feeds");
                    
                    JSONObject channel =  (JSONObject) jsonObject.get("channel");

                    LocalDate today = LocalDate.now();

                    for (int i = 0; i < feedsArray.length(); i++) {
                        JSONObject feedObject = feedsArray.getJSONObject(i);

                        int id = feedObject.getInt("entry_id");

                        // Lấy giá trị "field1" từ đối tượng feed
                        String field1 = feedObject.getString("field1");

                        // Lấy giá trị "created_at" từ đối tượng feed
                        String createdAt = feedObject.getString("created_at");
                        
                        int lastId = channel.getInt("last_entry_id");

                        // Chuyển đổi thời gian sang múi giờ +7 TPHCM
                        LocalDateTime localDateTime = convertToHCMTimeZone(createdAt);

                        LocalDate feedDate = localDateTime.toLocalDate();
                        if (feedDate.equals(today)) {
                            // Định dạng ngày và giờ thành chuỗi
                            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

                            String formattedDate = localDateTime.format(dateFormatter);
                            String formattedTime = localDateTime.format(timeFormatter);
                                                        
                      //       In ra thông tin để kiểm tra 
//                            System.out.println(id);
//                            System.out.println("time_in :" + time_in);
//                            System.out.println("time_out :" + time_out);
//                            System.out.println("Ngày: " + formattedDate);
//                            System.out.println("Giờ (múi giờ +7 TPHCM): " + formattedTime);

                            String cardId = field1;
                            String date = formattedDate;
                            Optional<Card> cardOptional = cardService.getCardById(cardId);
                            Card card = cardOptional.orElse(null);
                            Employee employee = null;
                            Account account = null;
                            if (card != null) {
                                String nameE = "";
                                String department = "";
                                employee = card.getEmployee();
                                if (employee != null) {
                                    nameE = employee.getName();
                                    department = card.getEmployee().getDepartment();
                                    account = accountService.getAccountByEmployee(employee);
                                    // Tiếp tục xử lý với employeeName
                                } else {
                                    System.out.println("Nhân viên không tồn tại");
                                }
                                long deviceId = 2;
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                java.util.Date parsedDate = dateFormat.parse(formattedDate);
                                java.sql.Date sqlDate = new java.sql.Date(parsedDate.getTime());

                                // Lưu history vào CSDL
                                History history = new History(id,account.getId(), cardId, deviceId, sqlDate, formattedTime);
                                historyService.saveHistory(history);

                                HistoryDto2 hisDto2 = new HistoryDto2(id, nameE, deviceId, cardId,
                                        department, formattedDate, formattedTime);
                                list.add(hisDto2);
                            } else {
//                                System.out.println("Không tồn tại thẻ Card");
                            }
                        }
                    }
                } else {
                    System.out.println("Lỗi: " + responseCode);
                }

                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return list;
        });

        try {
            // Chờ công việc hoàn tất và lấy kết quả
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            // Xử lý lỗi nếu cần thiết
            e.printStackTrace();
        }

        // Trả về một danh sách trống hoặc thông báo khác tùy theo yêu cầu của bạn.
        return new ArrayList<>();
    }

    // Thêm phương thức để dừng ExecutorService khi ứng dụng kết thúc
    @PreDestroy
    public void destroy() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }
    
    
	
	@GetMapping("/{day}")
	public ArrayList<HistoryDto2> getAllHistoryByDate(@PathVariable("day") String date){
		ArrayList<History> listHistory = new ArrayList<>();		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
		Date sqlDate = Date.valueOf(localDate);
		listHistory = historyService.getAllHistoryByDate(sqlDate);
		
		ArrayList<HistoryDto2> listHistoryDto2s = new ArrayList<>();
		for (History history : listHistory) {
	        HistoryDto2 historyDto2 = new HistoryDto2();
	        historyDto2.setId(history.getId());
	        historyDto2.seteName(history.getAccount().getEmployee().getName());
	        historyDto2.setDeviceId(history.getDevice().getId());;
	        historyDto2.setCardId(history.getCard().getId());;
	        historyDto2.setDepartment(history.getAccount().getEmployee().getDepartment());
	        historyDto2.setDate(String.valueOf(history.getDate()));
	        historyDto2.setTime_in(history.getTime_in());
	        String state = history.getTime_in().compareTo("13:00:00") < 0 ? "Đúng giờ" : "Muộn giờ";
	        historyDto2.setState(state);
	        // Sao chép các giá trị từ History sang HistoryDto

	        listHistoryDto2s.add(historyDto2);
	    }
		return listHistoryDto2s;
	}
}

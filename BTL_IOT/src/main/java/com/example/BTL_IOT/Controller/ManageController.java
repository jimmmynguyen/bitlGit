package com.example.BTL_IOT.Controller;



import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Sort;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.BTL_IOT.Model.Account;
import com.example.BTL_IOT.Model.Category;
import com.example.BTL_IOT.Model.Device;
import com.example.BTL_IOT.Model.DeviceDto;
import com.example.BTL_IOT.Model.Employee;
import com.example.BTL_IOT.Model.History;
import com.example.BTL_IOT.Model.HistoryDto;
import com.example.BTL_IOT.Model.Timekeeping;
import com.example.BTL_IOT.Model.WorkCalendar;
import com.example.BTL_IOT.Repository.AccountRepository;
import com.example.BTL_IOT.Service.AccountService;
import com.example.BTL_IOT.Service.CategoryService;
import com.example.BTL_IOT.Service.DeviceService;
import com.example.BTL_IOT.Service.EmployeeService;
import com.example.BTL_IOT.Service.FileService;
import com.example.BTL_IOT.Service.HistoryService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class ManageController {
	@Autowired
	private DeviceService deviceService;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private HistoryService historyService;
	
	@Autowired
	private EmployeeService employeeService;
	
	@GetMapping(value = "/manage")
	public String home(HttpSession session, Model model,HttpServletRequest request,
			@CookieValue(value = "c_user", defaultValue = "") String user,
			@CookieValue(value = "c_pass", defaultValue = "") String pass,
			@CookieValue(value = "c_rm", defaultValue = "0") String rm) {
		
		model.addAttribute("username", user);
		model.addAttribute("password", pass);
		model.addAttribute("rm", Integer.parseInt(rm));
		return "/login";
	}
	
	@GetMapping("manageHistory/page/{pageNumber}")
	public String manageAllHistory(@PathVariable("pageNumber") int currentPage, Model model) {
//		Page<HistoryDto> page = historyService.getAllHistoryforManage(currentPage);
//		int totalPage = page.getTotalPages();
//		if (currentPage > totalPage)currentPage-=1;
//		List<HistoryDto> listHistory= page.getContent();
//		model.addAttribute("listH", listHistory);
//		System.out.println(listHistory.size());
//		model.addAttribute("currentPage", currentPage);
//		model.addAttribute("totalPages",totalPage);
		return "/admin/manageHistoryInOut";
	}
	@GetMapping("manageDevice")
	public String manage( Model model) {
		Page<DeviceDto> page = deviceService.getAllDeviceforManage(1);
		int totalPage = page.getTotalPages();
		List<DeviceDto> listDevices = page.getContent();
		model.addAttribute("listD", listDevices);
		model.addAttribute("currentPage", 1);
		model.addAttribute("totalPages",totalPage);
		return "/admin/listDevices";
	}
	
	@GetMapping("manageDevice/page/{pageNumber}")
	public String manageDevice(@PathVariable("pageNumber") int currentPage, Model model) {
		Page<DeviceDto> page = deviceService.getAllDeviceforManage(currentPage);
		int totalPage = page.getTotalPages();
		if (currentPage > totalPage)page = deviceService.getAllDeviceforManage(totalPage);
		List<DeviceDto> listDevices = page.getContent();
		model.addAttribute("listD", listDevices);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("totalPages",totalPage);
		return "/admin/listDevices";
	}
	
	@GetMapping(value = "/history")
	public String history() {
		return "/admin/manageHistoryInOut";
	}
	
    @PostMapping(value = "/checkLogin")
	public String loginAdmin(HttpServletRequest req,HttpServletResponse res,Model model,HttpSession session) {
		String name = req.getParameter("user");
		String pass = req.getParameter("pass");
		String remember = req.getParameter("remember");
		// Tạo cookies
		Cookie cook_user = new Cookie("c_user", name);
        Cookie cook_pass = new Cookie("c_pass", pass);
        Cookie cook_remember = new Cookie("c_rm", remember);
        if (remember!=null){
            cook_user.setMaxAge(60*60*24*7);
            cook_pass.setMaxAge(60*60*24*7);
            cook_remember.setMaxAge(60*60*24*7);
        }
        else {
            cook_user.setMaxAge(0);
            cook_pass.setMaxAge(0);
            cook_remember.setMaxAge(0);
        }
        res.addCookie(cook_user);
        res.addCookie(cook_pass);
        res.addCookie(cook_remember);
        
        // Khởi tạo session Account
    
        Account ac = accountService.getAccount(name, pass);
        if (ac == null) {
            session.setAttribute("messAdmin", "Sai tài khoản hoặc mật khẩu");
        	session.removeAttribute("checkLoginAdmin");
            return "redirect:/manage";
        }
        else {
        	session.removeAttribute("checkLoginAdmin");
            session.setAttribute("account", ac); 
            session.removeAttribute("messAdmin");
            if (ac.getDuty()==1) {
            	
            	return manageAllHistory(1, model);
            }
            else return "/user/historyInOut";
        }    
		
	}
	
	@GetMapping(value = "/logoutAdmin")
	public String logout(HttpSession session) {
		session.removeAttribute("accountAdmin");
		session.removeAttribute("mess");
		session.setAttribute("checkLoginAdmin", 1);
		return"redirect:/manage";
	}
	
	@GetMapping(value = "/addDevice")
	public String addFormDevice (Model model) {
		ArrayList<Category> listCategory = new ArrayList<>();
		listCategory = categoryService.getAllCategory();
		Device device = new Device();
		model.addAttribute("checkMethod", -1);
		model.addAttribute("listC", listCategory);
		model.addAttribute("device", device);
		return "formDevice";
	}
	
	@PostMapping(value = "/saveDevice")
	public String saveBook(@ModelAttribute("Device") Device device,Model model,@RequestParam("cid") int cid,
			@RequestParam("photo")MultipartFile multipartFile,HttpSession session) throws IOException {
		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		if (!fileName.equals("")) {
			String uploadDir = "uploads/";
			FileService.saveFile(uploadDir, fileName, multipartFile);	
			device.setImage(fileName);
		}
		deviceService.createDevice(new Device(device.getName(), device.getSerial(), 
				device.getDescription(), cid, device.getImage(), device.getDay()));
		long count = deviceService.getQuantityDevice();
		int mod = (int) (count%8);
		if (mod==0) {
			return manageDevice((int) (count/8), model);
		}
		else return manageDevice((int) (count/8+1), model);
	}
	
	@GetMapping(value = "/viewDevice")
	public String editDevice(Model model,HttpServletRequest request) {
		String id = request.getParameter("did");
	    long cid = 0;
	    Optional<Device> deviceOptional = null;
	    Device device = null;
	    try {
	        long dId = Long.parseLong(id);
	        deviceOptional = deviceService.getDeviceById(dId);
	    } catch (Exception e) {
	        // Xử lý ngoại lệ nếu cần
	    }
	    
	    if (deviceOptional.isPresent()) {
	        device = deviceOptional.get();
	        cid = device.getCategory().getId();
	        model.addAttribute("device", device);
	    } else {
	    	System.out.println("Không tìm thấy device");
	    }

	    ArrayList<Category> listC = new ArrayList<>();
	    listC = categoryService.getAllBySort(cid);
	    model.addAttribute("listC", listC);
	    model.addAttribute("checkMethod", 1);
	    return "formDevice";
	}
	
	@GetMapping(value = "/deleteDevice")
	public String deleteDevice(HttpServletRequest request,Model model) {
		String id = request.getParameter("did");
		long loc = 0;
		try {
			long deviceId = Long.parseLong(id);
			deviceService.deleteDevice(deviceId);
			loc = deviceService.getIndexOfDeviceById(deviceId);
		} catch (Exception e) {
			// TODO: handle exception
		}

		if (loc != 0) {
			loc-=1;
			return manageDevice((int) (loc/8+1), model);
		}
		else return manageDevice(1, model);
	}
	
	@GetMapping(value = "/timekeeping")
	public String timekeeping(Model model) {
		ArrayList<WorkCalendar>list = new ArrayList<>();
		LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        int currentMonth = currentDate.getMonthValue();

        YearMonth yearMonth = YearMonth.of(currentYear, currentMonth);
        int daysInMonth = yearMonth.lengthOfMonth();
        for(int i=1;i<=daysInMonth;i++) {
        	String day ="";
        	if (i<=9)day = "0"+i;
        	else day = String.valueOf(i);
        	WorkCalendar wc = new WorkCalendar(day);
        	wc.setT(day, currentMonth, currentYear);
        	list.add(wc);
        }
        model.addAttribute("listC", list);
        
        ArrayList<Timekeeping> listTimekeepings = new ArrayList<>();
        ArrayList<Employee>listEmployees = new ArrayList<>();
        listEmployees = employeeService.getAllEmployees();
        for(Employee em : listEmployees) {
        	int c = Integer.parseInt(em.getId().substring(2));
        	String card = String.valueOf(c);
        	ArrayList<History> listHistorys = historyService.findAllHistoryByYearAndMonth(currentYear, currentMonth, card);
        	Timekeeping timekeeping = new Timekeeping(em);
        	ArrayList<Integer>listCalendar = new ArrayList<>();
        	for(int i=1;i<=daysInMonth;i++) {
        		boolean check = true;
        		for(History history : listHistorys) {
        			String dayStr = String.valueOf(history.getDate());
        			int day = Integer.parseInt(dayStr.substring(8));
        			if (day==i) {
        				listCalendar.add(1);check=false;break;
        			}
        		}
        		if (check)listCalendar.add(0);
        	}
        	timekeeping.setListTimekeeping(listCalendar);
        	timekeeping.setSum(listCalendar);
        	listTimekeepings.add(timekeeping);
        }
        model.addAttribute("listCa", listTimekeepings);
        model.addAttribute("monthY", currentMonth+"/"+currentYear);
		return "/admin/Timekeeping";
	}
	@GetMapping("/findTimekeeping")
    public String findTimekeeping(@RequestParam("monthYear") String monthYear,Model model) {
        int month = Integer.parseInt(monthYear.substring(0, 2));
        int year = Integer.parseInt(monthYear.substring(3));
        ArrayList<WorkCalendar>list = new ArrayList<>();
        YearMonth yearMonth = YearMonth.of(year, month);
        int daysInMonth = yearMonth.lengthOfMonth();
        for(int i=1;i<=daysInMonth;i++) {
        	String day ="";
        	if (i<=9)day = "0"+i;
        	else day = String.valueOf(i);
        	WorkCalendar wc = new WorkCalendar(day);
        	wc.setT(day, month, year);
        	list.add(wc);
        }
        model.addAttribute("listC", list);
        
        ArrayList<Timekeeping> listTimekeepings = new ArrayList<>();
        ArrayList<Employee>listEmployees = new ArrayList<>();
        listEmployees = employeeService.getAllEmployees();
        for(Employee em : listEmployees) {
        	int c = Integer.parseInt(em.getId().substring(2));
        	String card = String.valueOf(c);
        	ArrayList<History> listHistorys = historyService.findAllHistoryByYearAndMonth(year, month, card);
        	Timekeeping timekeeping = new Timekeeping(em);
        	ArrayList<Integer>listCalendar = new ArrayList<>();
        	for(int i=1;i<=daysInMonth;i++) {
        		boolean check = true;
        		for(History history : listHistorys) {
        			String dayStr = String.valueOf(history.getDate());
        			int day = Integer.parseInt(dayStr.substring(8));
        			if (day==i) {
        				listCalendar.add(1);check=false;break;
        			}
        		}
        		if (check)listCalendar.add(0);
        	}
        	timekeeping.setListTimekeeping(listCalendar);
        	timekeeping.setSum(listCalendar);
        	listTimekeepings.add(timekeeping);
        }
        model.addAttribute("listCa", listTimekeepings);
        model.addAttribute("date", monthYear);
        model.addAttribute("monthY", month+"/"+year);
        return "/admin/Timekeeping";
    }
	@GetMapping(value = "/control")
		public String deviceControl() {
			return "/admin/control";
		}
	
	@GetMapping(value = "/getStatistical")
	public String statistical(Model model) {
		LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        int currentMonth = currentDate.getMonthValue();

        YearMonth yearMonth = YearMonth.of(currentYear, currentMonth);
        int daysInMonth = yearMonth.lengthOfMonth();

        ArrayList<Timekeeping> listTimekeepings = new ArrayList<>();
        ArrayList<Employee>listEmployees = new ArrayList<>();
        listEmployees = employeeService.getAllEmployees();
        for(Employee em : listEmployees) {
        	int c = Integer.parseInt(em.getId().substring(2));
        	String card = String.valueOf(c);
        	ArrayList<History> listHistorys = historyService.findAllHistoryByYearAndMonth(currentYear, currentMonth, card);
        	Timekeeping timekeeping = new Timekeeping(em);
        	ArrayList<Integer>listCalendar = new ArrayList<>();
        	for(int i=1;i<=daysInMonth;i++) {
        		boolean check = true;
        		for(History history : listHistorys) {
        			String dayStr = String.valueOf(history.getDate());
        			int day = Integer.parseInt(dayStr.substring(8));
        			if (day==i) {
        				listCalendar.add(1);check=false;break;
        			}
        		}
        		if (check)listCalendar.add(0);
        	}
        	timekeeping.setListTimekeeping(listCalendar);
        	timekeeping.setSum(listCalendar);
        	listTimekeepings.add(timekeeping);
        }
        listTimekeepings.sort((o1, o2) -> o2.getSum()-o1.getSum());
        ArrayList<String> list3Name = new ArrayList<>();
        ArrayList<Integer> list3Sum = new ArrayList<>();
        ArrayList<String> list5Name = new ArrayList<>();
        ArrayList<Integer> list5Sum = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
			if (i<3) {
				list3Name.add(listTimekeepings.get(i).getEmployee().getName());
				list3Sum.add(listTimekeepings.get(i).getSum());
			}
			list5Name.add(listTimekeepings.get(i).getEmployee().getName());
			list5Sum.add(listTimekeepings.get(i).getSum());
		}
        model.addAttribute("list3N", list3Name);
        model.addAttribute("list3S", list3Sum);
        model.addAttribute("list5N", list5Name);
        model.addAttribute("list5S", list5Sum);
        model.addAttribute("monthY", currentMonth+"/"+currentYear);
		return "/admin/statistical";
	}
	
	@GetMapping("/findStatistical")
    public String fStatistical(@RequestParam("monthYear") String monthYear,Model model) {
        int month = Integer.parseInt(monthYear.substring(0, 2));
        int year = Integer.parseInt(monthYear.substring(3));
        ArrayList<WorkCalendar>list = new ArrayList<>();
        YearMonth yearMonth = YearMonth.of(year, month);
        int daysInMonth = yearMonth.lengthOfMonth();
        for(int i=1;i<=daysInMonth;i++) {
        	String day ="";
        	if (i<=9)day = "0"+i;
        	else day = String.valueOf(i);
        	WorkCalendar wc = new WorkCalendar(day);
        	wc.setT(day, month, year);
        	list.add(wc);
        }
        model.addAttribute("listC", list);
        
        ArrayList<Timekeeping> listTimekeepings = new ArrayList<>();
        ArrayList<Employee>listEmployees = new ArrayList<>();
        listEmployees = employeeService.getAllEmployees();
        for(Employee em : listEmployees) {
        	int c = Integer.parseInt(em.getId().substring(2));
        	String card = String.valueOf(c);
        	ArrayList<History> listHistorys = historyService.findAllHistoryByYearAndMonth(year, month, card);
        	Timekeeping timekeeping = new Timekeeping(em);
        	ArrayList<Integer>listCalendar = new ArrayList<>();
        	for(int i=1;i<=daysInMonth;i++) {
        		boolean check = true;
        		for(History history : listHistorys) {
        			String dayStr = String.valueOf(history.getDate());
        			int day = Integer.parseInt(dayStr.substring(8));
        			if (day==i) {
        				listCalendar.add(1);check=false;break;
        			}
        		}
        		if (check)listCalendar.add(0);
        	}
        	timekeeping.setListTimekeeping(listCalendar);
        	timekeeping.setSum(listCalendar);
        	listTimekeepings.add(timekeeping);
        }
        listTimekeepings.sort((o1, o2) -> o2.getSum()-o1.getSum());
        ArrayList<String> list3Name = new ArrayList<>();
        ArrayList<Integer> list3Sum = new ArrayList<>();
        ArrayList<String> list5Name = new ArrayList<>();
        ArrayList<Integer> list5Sum = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
			if (i<3) {
				list3Name.add(listTimekeepings.get(i).getEmployee().getName());
				list3Sum.add(listTimekeepings.get(i).getSum());
			}
			list5Name.add(listTimekeepings.get(i).getEmployee().getName());
			list5Sum.add(listTimekeepings.get(i).getSum());
		}
        model.addAttribute("list3N", list3Name);
        model.addAttribute("list3S", list3Sum);
        model.addAttribute("list5N", list5Name);
        model.addAttribute("list5S", list5Sum);
        model.addAttribute("date", monthYear);
        model.addAttribute("monthY", month+"/"+year);
        return "/admin/statistical";
    }
}

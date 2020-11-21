package mvc.postgres.web.control;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import mvc.postgres.web.models.ShohinEntity;
import mvc.postgres.web.models.ShohinEntityRepository;

@Controller
public class ShohinController {
	@Autowired
	ShohinEntityRepository repository;

	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public ModelAndView index(ModelAndView mandv) {
		mandv.addObject("btitle", "商品一覧表(全件)");
		List<ShohinEntity> data = repository.findAll(Sort.by(Direction.ASC, "_NumId"));
		mandv.addObject("data", data);
		mandv.setViewName("view");
		return mandv;
	}

	@RequestMapping(value = "/view", method = RequestMethod.POST)
	public ModelAndView form(@RequestParam("find")Short find, ModelAndView mandv) {
		mandv.addObject("btitle", "商品一覧(抽出)");
		mandv.addObject("value", find);
		List<ShohinEntity> data = repository.findBy_ShohinCode(find);
		mandv.addObject("data", data);
		mandv.setViewName("view");
		return mandv;
	}
	
	@RequestMapping(value = "/editc/{numid}", method = RequestMethod.GET)
	public ModelAndView edit(@PathVariable("numid")Integer numid, ModelAndView mandv) {
		mandv.addObject("btitle", "商品編集");
		ShohinEntity data = repository.getOne(numid);
		mandv.addObject("data", data);
		mandv.setViewName("edit");
		return mandv;
	}
	
	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public String create(@ModelAttribute ShohinEntity shohin) {
		String daytime = getDaytime();
		shohin.setEditDate(BigDecimal.valueOf(Integer.valueOf(daytime.substring(0,8))));
		shohin.setEditTime(BigDecimal.valueOf(Integer.valueOf(daytime.substring(8))));
		repository.save(shohin);
		return "redirect:/view";
	}
	
	@RequestMapping(value = "/update/{numid}", method = RequestMethod.POST)
	public String update(@PathVariable("numid")Integer numid, @ModelAttribute ShohinEntity shohin) {
		shohin.setNumId(numid);
		String daytime = getDaytime();
		shohin.setEditDate(BigDecimal.valueOf(Integer.valueOf(daytime.substring(0,8))));
		shohin.setEditTime(BigDecimal.valueOf(Integer.valueOf(daytime.substring(8))));
		repository.save(shohin);
		return "redirect:/view";
	}
	
	@RequestMapping(value = "/delete/{numid}", method = RequestMethod.POST)
	public String del(@PathVariable("numid")Integer numid) {
		repository.deleteById(numid);
		return "redirect:/view";
	}
	
	private String getDaytime() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		String daytime = formatter.format(java.time.LocalDateTime.now());
		return daytime;
	}
}
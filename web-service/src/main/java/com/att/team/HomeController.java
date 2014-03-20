package com.att.team;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.att.team.dtos.CirclesDto;
import com.att.team.dtos.ConnectionsDto;
import com.att.team.dtos.MemberDto;
import com.att.team.dtos.RequestDto;
import com.att.team.dtos.ResponseDto;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

	
	private static final Logger logger = LoggerFactory
			.getLogger(HomeController.class);

	
	@Autowired
	TeamService mTeamService;
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);

		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG,
				DateFormat.LONG, locale);

		String formattedDate = dateFormat.format(date);

		model.addAttribute("serverTime", formattedDate);

		return "home";
	}

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/v1/agent/deviceInRange", method = RequestMethod.POST)
	@ResponseBody
	public ResponseDto updateDeviceInRange(@RequestBody RequestDto requestDto) {


		return mTeamService.memberDataReceived(requestDto);
	}
	

	@RequestMapping(value = "/v1/webapp/circles", method = RequestMethod.GET)
	@ResponseBody
	public CirclesDto getCircles() {

		return mTeamService.getCirclesDto();
	}
	
	
	@RequestMapping(value = "/v1/webapp/connections", method = RequestMethod.GET)
	@ResponseBody
	public ConnectionsDto getConnections() {

		return mTeamService.getConnectionsDto();
	}
}

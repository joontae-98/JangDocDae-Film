package com.example.jangdocdaefilm.controller;

import com.example.jangdocdaefilm.dto.MovieDto;
import com.example.jangdocdaefilm.dto.MoviesDto;
import com.example.jangdocdaefilm.dto.RecomDto;
import com.example.jangdocdaefilm.dto.RecomMovieDto;
import com.example.jangdocdaefilm.service.MovieService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/movie")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @Value("${jangDocDae.tmdb.json.Url}")
    private String serviceUrl;

    @RequestMapping(value = "/search/{keyword}", method = RequestMethod.GET)
    public ModelAndView searchMoviePage(@PathVariable String keyword, @RequestParam(value = "page", required = false, defaultValue = "1") String page) throws Exception {
        ModelAndView mv = new ModelAndView("movie/searchResult");

        String utf8Keyword = URLEncoder.encode(keyword, "UTF-8");


        MoviesDto movies = movieService.getSearchMovies(serviceUrl + "search/movie?query=" + utf8Keyword + "&include_adult=false&language=ko&page=" + page);
//        List<MovieDto> movieList = movies.getResults();
//
//        mv.addObject("movieList", movieList);
//        mv.addObject("totalPage", movies.getTotal_pages());
//        mv.addObject("currentPage", movies.getPage());
//        mv.addObject("maxPage", 5);

        mv.addObject("totalResults", movies.getTotal_results());
        mv.addObject("keyword", keyword);

        return mv;
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Object getSearchMovie(@RequestParam("keyword") String keyword, @RequestParam("page") String page) throws Exception {
        String utf8Keyword = URLEncoder.encode(keyword, "UTF-8");
        MoviesDto movies = movieService.getSearchMovies(serviceUrl + "search/movie?query=" + utf8Keyword + "&include_adult=false&language=ko&page=" + page);
        return movies;
    }

    //페이지 로딩 함수
    @RequestMapping(value = "/category", method = RequestMethod.GET)
    public ModelAndView categoryPage() throws Exception {
        ModelAndView mv = new ModelAndView("movie/category");
        return mv;
    }


    //카테고리 검색 함수
    @RequestMapping(value = "/category/search", method = RequestMethod.GET)
    public Object categorySearchMovie(@RequestParam("year") String year, @RequestParam("genre") String genre, @RequestParam("page") String page) throws Exception {

        String url = "discover/movie?include_adult=false&include_video=false&language=ko&page=" + page + "&sort_by=popularity.desc";
        String yearQuery = year != null ? "&primary_release_year=" + year : "";
        String genreQuery = genre != null ? "&with_genres=" + genre : "";

        MoviesDto movies = movieService.getSearchMovies(serviceUrl + url + yearQuery + genreQuery);
        return movies;
    }


    @RequestMapping(value = "/recom/set", method = RequestMethod.GET)
    public ModelAndView recomSetPage() throws Exception {
        ModelAndView mv = new ModelAndView("movie/recommendSet");
        return mv;
    }

    @RequestMapping(value = "/recom/search", method = RequestMethod.GET)
    public Object recomSearchMovie(@RequestParam("year") String year, @RequestParam("genre") String genre, @RequestParam("keyword") String keyword, @RequestParam("page") String page) throws Exception {

        String url = serviceUrl;

        if (!keyword.equals("")) {
            String utf8Keyword = URLEncoder.encode(keyword, "UTF-8");
            url += "search/movie?query=" + utf8Keyword + "&include_adult=false&language=ko&page=1";
        } else {
            String searchUrl = "discover/movie?include_adult=false&include_video=false&language=ko&page=" + page + "&sort_by=popularity.desc";
            String yearQuery = year != null ? "&primary_release_year=" + year : "";
            String genreQuery = genre != null ? "&with_genres=" + genre : "";
            url += searchUrl + yearQuery + genreQuery;
        }

        MoviesDto movies = movieService.getSearchMovies(url);
        return movies;
    }

    @RequestMapping(value = "/recom/set", method = RequestMethod.POST)
    public void recomInsert(@RequestParam("title") String title, @RequestParam("content") String content, @RequestParam("movies[]") String[] movies, HttpServletRequest req) throws Exception {
        String userName = (String) req.getSession().getAttribute("userName");
//        // 1. recoms 생성
        int idx = movieService.insertRecoms(title, content, userName);
//        // 2. recom 생성
        movieService.insertRecom(movies, idx);
    }

    @RequestMapping(value = "/recoms/like", method = RequestMethod.GET)
    public Object isRecomsLike(@RequestParam("idx") String idx, HttpServletRequest req) throws Exception {
        String userName = (String) req.getSession().getAttribute("userName");
        int result = movieService.isRecomsLike(idx, userName);
        return result;
    }

    @RequestMapping(value = "/recoms/like", method = RequestMethod.POST)
    public void insertRecomsLike(@RequestParam("idx") String idx, HttpServletRequest req) throws Exception {
        String userName = (String) req.getSession().getAttribute("userName");
        movieService.insertRecomsLike(idx, userName);
    }

    @RequestMapping(value = "/recoms/like", method = RequestMethod.PUT)
    public void updateRecomsLike(@RequestParam("idx") String idx, HttpServletRequest req) throws Exception {
        String userName = (String) req.getSession().getAttribute("userName");
        movieService.updateRecomsLike(idx, userName);
    }


    @RequestMapping(value = "/recoms/like", method = RequestMethod.DELETE)
    public void deleteRecomsLike(@RequestParam("idx") String idx, HttpServletRequest req) throws Exception {
        String userName = (String) req.getSession().getAttribute("userName");
        movieService.deleteRecomsLike(idx, userName);
    }

    @RequestMapping(value = "/recom/{idx}", method = RequestMethod.GET)
    public ModelAndView recommendPage(@PathVariable int idx) throws Exception {
        ModelAndView mv = new ModelAndView("movie/recommendDetail");
        List<RecomMovieDto> movies = new ArrayList<>();

        RecomDto recom = movieService.selectRecom(idx);
        recom.setPoster(movieService.setPosterPath(serviceUrl + "movie/" + recom.getMovieId() + "?language=ko"));
        List<String> movieIds = movieService.selectMovieIds(idx);
        for (String movieId : movieIds) {
            movies.add(movieService.getMovie(serviceUrl + "movie/" + movieId + "?language=ko"));
        }

        mv.addObject("recom", recom);
        mv.addObject("movies", movies);

        return mv;
    }


    @RequestMapping(value = "/recom/{idx}", method = RequestMethod.DELETE)
    public void deleteRecoms(@PathVariable int idx) throws Exception {
        movieService.deleteRecoms(idx);
    }
}

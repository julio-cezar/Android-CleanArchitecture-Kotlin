package com.fernandocejas.sample.features.movies

import android.content.Context
import com.fernandocejas.sample.framework.interactor.UseCaseObserver
import javax.inject.Inject

class MovieDetailsPresenter
@Inject constructor(private val getMovieDetails: GetMovieDetails,
                    private val playMovie: PlayMovie) {

    internal lateinit var movieDetailsView: MovieDetailsView

    fun destroy() {
        getMovieDetails.dispose()
        playMovie.dispose()
        movieDetailsView.dispose()
    }

    fun loadMovieDetails(movieId: Int) {
        movieDetailsView.showLoading()
        getMovieDetails.execute(MovieDetailsObserver(), GetMovieDetails.Params(movieId))
    }

    fun playMovie(context: Context, url: String) {
        playMovie.execute(PlayMovie.Params(context, url))
    }

    internal inner class MovieDetailsObserver : UseCaseObserver.RxObservable<MovieDetails>() {
        override fun onComplete() = movieDetailsView.hideLoading()
        override fun onNext(value: MovieDetails) {
            val movieDetails = MovieDetailsViewModel(value.id, value.title, value.poster,
                    value.summary, value.cast, value.director, value.year, value.trailer)
            movieDetailsView.renderDetails(movieDetails)
        }
        override fun onError(e: Throwable) = TODO()
    }
}

package com.campusmov.uniride.data.repository.route

import com.campusmov.uniride.data.datasource.remote.dto.RouteRequestDto
import com.campusmov.uniride.data.datasource.remote.service.RouteService
import com.campusmov.uniride.domain.route.model.Intersection
import com.campusmov.uniride.domain.route.model.Route
import com.campusmov.uniride.domain.route.repository.RouteRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RouteRepositoryImpl @Inject constructor(
    private val routeService: RouteService
) : RouteRepository {

    override suspend fun getRoute(
        startLatitude: Double,
        startLongitude: Double,
        endLatitude: Double,
        endLongitude: Double,
        algorithm: String
    ): Route {
        val request = RouteRequestDto(
            startLatitude = startLatitude,
            startLongitude = startLongitude,
            endLatitude = endLatitude,
            endLongitude = endLongitude
        )

        val response = when (algorithm) {
            "a-star" -> routeService.getRouteAStar(request)
            else -> routeService.getRouteDijkstra(request)
        }

        return Route(
            intersections = response.intersections.map {
                Intersection(it.latitude, it.longitude)
            },
            totalDistance = response.totalDistance,
            totalDuration = response.totalDuration
        )
    }
    //TODO: DELETE HARDCODED DATA

    override suspend fun getRoute(): Route {
        val intersections = listOf(
            Intersection(-11.9967588, -77.0074882),
            Intersection(-11.9966047, -77.0075285),
            Intersection(-11.9969647, -77.0091086),
            Intersection(-11.9970574, -77.0094672),
            Intersection(-11.9970838, -77.0095614),
            Intersection(-11.9971439, -77.0097906),
            Intersection(-11.9973176, -77.0097379),
            Intersection(-12.001568, -77.0085929),
            Intersection(-12.0059742, -77.0061255),
            Intersection(-12.006096, -77.0060481),
            Intersection(-12.0131433, -77.0021517),
            Intersection(-12.0132448, -77.0021428),
            Intersection(-12.0171184, -77.0021794),
            Intersection(-12.0175516, -77.0021453),
            Intersection(-12.0179499, -77.002018),
            Intersection(-12.0180451, -77.0019861),
            Intersection(-12.0189669, -77.0016457),
            Intersection(-12.0202251, -77.0014558),
            Intersection(-12.0207356, -77.0015234),
            Intersection(-12.022555, -77.0017475),
            Intersection(-12.0241489, -77.001949),
            Intersection(-12.0247276, -77.0020421),
            Intersection(-12.0252817, -77.0019911),
            Intersection(-12.0257105, -77.0018477),
            Intersection(-12.0261921, -77.0016925),
            Intersection(-12.0272191, -77.0013575),
            Intersection(-12.0277379, -77.0011859),
            Intersection(-12.027825, -77.0011546),
            Intersection(-12.028775, -77.0008023),
            Intersection(-12.0290385, -77.0006323),
            Intersection(-12.0292421, -77.0004974),
            Intersection(-12.0295789, -77.0002572),
            Intersection(-12.0301673, -76.9998644),
            Intersection(-12.0301416, -76.999709),
            Intersection(-12.0300306, -76.9991665),
            Intersection(-12.0297132, -76.9984348),
            Intersection(-12.0295766, -76.9980941),
            Intersection(-12.0291697, -76.9967444),
            Intersection(-12.029528, -76.9954947),
            Intersection(-12.0320467, -76.9917809),
            Intersection(-12.0415859, -76.9872738),
            Intersection(-12.0551802, -76.9749045),
            Intersection(-12.058145, -76.9736156),
            Intersection(-12.0668582, -76.9717316),
            Intersection(-12.0674179, -76.9719343),
            Intersection(-12.0704534, -76.9729317),
            Intersection(-12.0711556, -76.9732161),
            Intersection(-12.0727843, -76.9738881),
            Intersection(-12.0729057, -76.9739403),
            Intersection(-12.0763363, -76.9753659),
            Intersection(-12.077003, -76.9756148),
            Intersection(-12.077738, -76.9759239),
            Intersection(-12.0779407, -76.9760098),
            Intersection(-12.0833777, -76.9807508),
            Intersection(-12.0852471, -76.9822714),
            Intersection(-12.0890703, -76.9813271),
            Intersection(-12.0945439, -76.9805435),
            Intersection(-12.0959089, -76.9803383),
            Intersection(-12.0962584, -76.9802825),
            Intersection(-12.0970048, -76.9803067),
            Intersection(-12.0976029, -76.9802834),
            Intersection(-12.0985928, -76.9771211),
            Intersection(-12.0983987, -76.9757338),
            Intersection(-12.1001543, -76.9764402),
            Intersection(-12.1010553, -76.9767298),
            Intersection(-12.1011626, -76.976773),
            Intersection(-12.1017889, -76.9751519),
            Intersection(-12.1018756, -76.9735485),
            Intersection(-12.1024356, -76.9721166),
            Intersection(-12.1037882, -76.9726522),
            Intersection(-12.1040247, -76.9721788),
            Intersection(-12.1043485, -76.9716961),
            Intersection(-12.1045815, -76.9713375),
            Intersection(-12.1049226, -76.9708159),
            Intersection(-12.1049785, -76.970731),
            Intersection(-12.1061693, -76.9693196),
            Intersection(-12.1060058, -76.9682568),
            Intersection(-12.1063366, -76.9683688),
            Intersection(-12.1070008, -76.9680573),
            Intersection(-12.1070554, -76.9680217),
            Intersection(-12.1056839, -76.9661071),
            Intersection(-12.1051468, -76.9654247),
            Intersection(-12.1051459, -76.9653715),
            Intersection(-12.1050819, -76.9652752),
            Intersection(-12.1050242, -76.9652528),
            Intersection(-12.1046121, -76.9646577),
            Intersection(-12.1032225, -76.9628072)
        )

        return Route(
            intersections = intersections,
            totalDistance = 3357.194990849014,
            totalDuration = 0.0
        )
    }
}

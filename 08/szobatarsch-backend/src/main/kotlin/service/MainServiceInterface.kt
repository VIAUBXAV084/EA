package hu.bme.kszk.service

import hu.bme.kszk.dto.rooms.RoomInviteRequestsRequest
import hu.bme.kszk.service.rooms.blocked.BlockedRoomsServiceInterface
import hu.bme.kszk.service.rooms.hidden.HiddenRoomServiceInterface
import hu.bme.kszk.service.rooms.regular.RoomsServiceInterface
import hu.bme.kszk.service.rooms.reports.RoomReportsServiceInterface
import hu.bme.kszk.service.rooms.roominviterequest.RoomInviteRequestsServiceInterface
import hu.bme.kszk.service.usecases.UsecasesServiceInterface
import hu.bme.kszk.service.rooms.roommergeapprove.RoomMergeApprovesServiceInterface
import hu.bme.kszk.service.rooms.roommergerequest.RoomMergeRequestServiceInterface
import hu.bme.kszk.service.users.admins.AdminsServiceInterface
import hu.bme.kszk.service.users.blockedusers.BlockedUsersServiceInterface
import hu.bme.kszk.service.users.bros.BroServiceInterface
import hu.bme.kszk.service.users.contactinfos.ContactInfoService
import hu.bme.kszk.service.users.userpreferences.UserPreferencesService
import hu.bme.kszk.service.users.userreports.UserReportsServiceInterface
import hu.bme.kszk.service.users.users.UsersServiceInterface


interface MainServiceInterface {
    val roomsService: RoomsServiceInterface
    val blockedRoomsService: BlockedRoomsServiceInterface
    val roomReportsService: RoomReportsServiceInterface
    val hiddenRoomService: HiddenRoomServiceInterface


    val roomMergeApprovesService: RoomMergeApprovesServiceInterface
    val roomMergeRequestService: RoomMergeRequestServiceInterface
    val roomInviteRequestsService: RoomInviteRequestsServiceInterface
    val usersService: UsersServiceInterface
    val contactInfoService: ContactInfoService
    val adminService: AdminsServiceInterface
    val broService: BroServiceInterface
    val usersPreferencesService: UserPreferencesService
    val userReportsService: UserReportsServiceInterface
    val blockedUsersService: BlockedUsersServiceInterface

    val usecasesService: UsecasesServiceInterface
}